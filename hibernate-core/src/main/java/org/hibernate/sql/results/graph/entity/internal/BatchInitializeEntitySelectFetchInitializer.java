/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.sql.results.graph.entity.internal;

import java.util.HashMap;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.mapping.internal.ToOneAttributeMapping;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.spi.NavigablePath;
import org.hibernate.sql.results.graph.AssemblerCreationState;
import org.hibernate.sql.results.graph.DomainResult;
import org.hibernate.sql.results.graph.Initializer;
import org.hibernate.sql.results.graph.InitializerData;
import org.hibernate.sql.results.graph.InitializerParent;
import org.hibernate.sql.results.jdbc.spi.RowProcessingState;

import static org.hibernate.internal.log.LoggingHelper.toLoggableString;

/**
 * Loads entities from the persistence context or creates proxies if not found there,
 * and initializes all proxies in a batch.
 */
public class BatchInitializeEntitySelectFetchInitializer extends AbstractBatchEntitySelectFetchInitializer<BatchInitializeEntitySelectFetchInitializer.BatchInitializeEntitySelectFetchInitializerData>
	implements Initializer.BatchLoadConsumer<Object> {

	public static class BatchInitializeEntitySelectFetchInitializerData extends AbstractBatchEntitySelectFetchInitializerData {
		private HashMap<EntityKey, Object> toBatchLoad;

		public BatchInitializeEntitySelectFetchInitializerData(
				BatchInitializeEntitySelectFetchInitializer initializer,
				RowProcessingState rowProcessingState) {
			super( initializer, rowProcessingState );
		}
	}

	public BatchInitializeEntitySelectFetchInitializer(
			InitializerParent<?> parent,
			ToOneAttributeMapping referencedModelPart,
			NavigablePath fetchedNavigable,
			EntityPersister concreteDescriptor,
			DomainResult<?> keyResult,
			boolean affectedByFilter,
			AssemblerCreationState creationState) {
		super( parent, referencedModelPart, fetchedNavigable, concreteDescriptor, keyResult, affectedByFilter, creationState );
	}

	@Override
	protected InitializerData createInitializerData(RowProcessingState rowProcessingState) {
		return new BatchInitializeEntitySelectFetchInitializerData( this, rowProcessingState );
	}

	@Override
	protected void registerToBatchFetchQueue(BatchInitializeEntitySelectFetchInitializerData data) {
		super.registerToBatchFetchQueue( data );
		// Force creating a proxy
		final var entityKey = data.entityKey;
		final Object instance =
				data.getRowProcessingState().getSession()
						.internalLoad( entityKey.getEntityName(), entityKey.getIdentifier(), false, false );
		data.setInstance( instance );
		var toBatchLoad = data.toBatchLoad;
		if ( toBatchLoad == null ) {
			toBatchLoad = data.toBatchLoad = new HashMap<>();
		}
		toBatchLoad.put( entityKey, null );
	}

	@Override
	public @Nullable BatchLoadBlockingRunnable<?> getBatchLoad(BatchInitializeEntitySelectFetchInitializerData data) {
		final var toBatchLoad = data.toBatchLoad;
		return toBatchLoad != null
			? new BatchLoadBlockingRunnable<>( toBatchLoad, this )
				: null;
	}

	@Override
	public void endLoading(BatchInitializeEntitySelectFetchInitializerData data) {
		super.endLoading( data );
		final var keysToBatchLoad = data.toBatchLoad;
		if ( keysToBatchLoad != null ) {
			final RowProcessingState rowProcessingState = data.getRowProcessingState();
			final SharedSessionContractImplementor session = rowProcessingState.getSession();
			for ( var entityKey : keysToBatchLoad.keySet() ) {
				final Object instance = loadInstance( entityKey, toOneMapping, session );
				postBatchLoad( entityKey, instance, null, rowProcessingState );
			}
			data.toBatchLoad = null;
		}
	}

	@Override
	public void postBatchLoad(EntityKey entityKey, Object entity, Object noop, RowProcessingState rowProcessingState) {
		if ( entity == null ) {
			checkNotFound( toOneMapping, affectedByFilter, entityKey.getEntityName(), entityKey.getIdentifier() );
		}
	}

	@Override
	public String toString() {
		return "BatchInitializeEntitySelectFetchInitializer("
				+ toLoggableString( getNavigablePath() ) + ")";
	}

}
