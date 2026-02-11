/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.sql.results.graph.entity.internal;

import java.util.HashMap;
import java.util.HashSet;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.metamodel.mapping.internal.ToOneAttributeMapping;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.spi.NavigablePath;
import org.hibernate.sql.results.graph.AssemblerCreationState;
import org.hibernate.sql.results.graph.DomainResult;
import org.hibernate.sql.results.graph.InitializerData;
import org.hibernate.sql.results.graph.InitializerParent;
import org.hibernate.sql.results.jdbc.spi.RowProcessingState;

import static org.hibernate.internal.log.LoggingHelper.toLoggableString;

/**
 * Loads entities from the persistence context or creates proxies if not found there,
 * and initializes all proxies in a batch.
 */
public class BatchInitializeEntitySelectFetchInitializer extends AbstractBatchEntitySelectFetchInitializer<BatchInitializeEntitySelectFetchInitializer.BatchInitializeEntitySelectFetchInitializerData> {

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
	protected void registerResolutionListener(BatchInitializeEntitySelectFetchInitializerData data) {
		// No-op, because we resolve a proxy
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
			? new BatchLoadBlockingRunnable<>( toBatchLoad, this::postLoad )
				: null;
	}

	@Override
	public void endLoading(BatchInitializeEntitySelectFetchInitializerData data) {
		super.endLoading( data );
		final var keysToBatchLoad = data.toBatchLoad;
		if ( keysToBatchLoad != null ) {
			for ( var entityKey : keysToBatchLoad.keySet() ) {
				postLoad( entityKey, null, data.getRowProcessingState() );
			}
			data.toBatchLoad = null;
		}
	}

	private void postLoad(EntityKey entityKey, Object noop, RowProcessingState rowProcessingState) {
		loadInstance( entityKey, toOneMapping, affectedByFilter, rowProcessingState.getSession() );
	}

	@Override
	public String toString() {
		return "BatchInitializeEntitySelectFetchInitializer("
				+ toLoggableString( getNavigablePath() ) + ")";
	}

}
