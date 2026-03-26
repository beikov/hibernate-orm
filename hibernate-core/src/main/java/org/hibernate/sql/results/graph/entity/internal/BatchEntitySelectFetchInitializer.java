/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.sql.results.graph.entity.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.mapping.AttributeMapping;
import org.hibernate.metamodel.mapping.internal.ToOneAttributeMapping;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.spi.NavigablePath;
import org.hibernate.sql.results.graph.AssemblerCreationState;
import org.hibernate.sql.results.graph.DomainResult;
import org.hibernate.sql.results.graph.Initializer;
import org.hibernate.sql.results.graph.InitializerData;
import org.hibernate.sql.results.graph.InitializerParent;
import org.hibernate.sql.results.jdbc.spi.RowProcessingState;
import org.hibernate.type.Type;

import static org.hibernate.internal.log.LoggingHelper.toLoggableString;

public class BatchEntitySelectFetchInitializer extends AbstractBatchEntitySelectFetchInitializer<BatchEntitySelectFetchInitializer.BatchEntitySelectFetchInitializerData>
		implements Initializer.BatchLoadConsumer<List<BatchEntitySelectFetchInitializer.ParentInfo>> {
	protected final AttributeMapping[] parentAttributes;
	protected final Setter referencedModelPartSetter;
	protected final Type referencedModelPartType;

	public static class BatchEntitySelectFetchInitializerData extends AbstractBatchEntitySelectFetchInitializerData {
		private HashMap<EntityKey, List<ParentInfo>> toBatchLoad;

		public BatchEntitySelectFetchInitializerData(
				BatchEntitySelectFetchInitializer initializer,
				RowProcessingState rowProcessingState) {
			super( initializer, rowProcessingState );
		}
	}

	public BatchEntitySelectFetchInitializer(
			InitializerParent<?> parentAccess,
			ToOneAttributeMapping referencedModelPart,
			NavigablePath fetchedNavigable,
			EntityPersister concreteDescriptor,
			DomainResult<?> keyResult,
			boolean affectedByFilter,
			AssemblerCreationState creationState) {
		super( parentAccess, referencedModelPart, fetchedNavigable, concreteDescriptor, keyResult, affectedByFilter, creationState );
		parentAttributes = getParentEntityAttributes( referencedModelPart.getAttributeName() );
		referencedModelPartSetter = referencedModelPart.getPropertyAccess().getSetter();
		referencedModelPartType =
				referencedModelPart.findContainingEntityMapping().getEntityPersister()
						.getPropertyType( referencedModelPart.getAttributeName() );
	}

	@Override
	protected InitializerData createInitializerData(RowProcessingState rowProcessingState) {
		return new BatchEntitySelectFetchInitializerData( this, rowProcessingState );
	}

	@Override
	protected void registerToBatchFetchQueue(BatchEntitySelectFetchInitializerData data) {
		super.registerToBatchFetchQueue( data );
		final var rowProcessingState = data.getRowProcessingState();
		final var owningData = owningEntityInitializer.getData( rowProcessingState );
		var toBatchLoad = data.toBatchLoad;
		if ( toBatchLoad == null ) {
			toBatchLoad = data.toBatchLoad = new HashMap<>();
		}
		// Always register the entity key for resolution
		final var parentInfos = toBatchLoad.computeIfAbsent( data.entityKey, key -> new ArrayList<>() );
		// But only add the parent info if the parent entity is not already initialized
		if ( owningData.getState() != State.INITIALIZED ) {
			final var parentAttribute =
					parentAttributes[owningEntityInitializer.getConcreteDescriptor( owningData )
							.getSubclassId()];
			if ( parentAttribute != null ) {
				parentInfos.add( new ParentInfo(
						owningEntityInitializer.getTargetInstance( owningData ),
						parentAttribute.getStateArrayPosition()
				) );
			}
		}
	}

	static class ParentInfo {
		private final Object parentInstance;
		private final int propertyIndex;

		public ParentInfo(Object parentInstance, int propertyIndex) {
			this.parentInstance = parentInstance;
			this.propertyIndex = propertyIndex;
		}
	}

	@Override
	public @Nullable BatchLoadBlockingRunnable<?> getBatchLoad(BatchEntitySelectFetchInitializerData data) {
		final var toBatchLoad = data.toBatchLoad;
		return toBatchLoad != null
			? new BatchLoadBlockingRunnable<>( toBatchLoad, this )
				: null;
	}

	@Override
	public void endLoading(BatchEntitySelectFetchInitializerData data) {
		super.endLoading( data );
		final var toBatchLoad = data.toBatchLoad;
		if ( toBatchLoad != null ) {
			final RowProcessingState rowProcessingState = data.getRowProcessingState();
			final SharedSessionContractImplementor session = rowProcessingState.getSession();
			for ( var entry : toBatchLoad.entrySet() ) {
				final Object instance = loadInstance( entry.getKey(), toOneMapping, session );
				postBatchLoad( entry.getKey(), instance, entry.getValue(), rowProcessingState );
			}
			data.toBatchLoad = null;
		}
	}

	@Override
	public void postBatchLoad(EntityKey entityKey, Object instance, List<ParentInfo> parentInfos, RowProcessingState rowProcessingState) {
		if ( instance == null ) {
			checkNotFound( toOneMapping, affectedByFilter, entityKey.getEntityName(), entityKey.getIdentifier() );
		}
		final SharedSessionContractImplementor session = rowProcessingState.getSession();
		final SessionFactoryImplementor factory = session.getFactory();
		final PersistenceContext persistenceContext = session.getPersistenceContextInternal();
		for ( var parentInfo : parentInfos ) {
			final Object parentInstance = parentInfo.parentInstance;
			final var entityEntry = persistenceContext.getEntry( parentInstance );
			referencedModelPartSetter.set( parentInstance, instance );
			final var loadedState = entityEntry.getLoadedState();
			if ( loadedState != null ) {
				loadedState[parentInfo.propertyIndex] =
						referencedModelPartType.deepCopy( instance, factory );
			}
		}
	}

	@Override
	public String toString() {
		return "BatchEntitySelectFetchInitializer("
				+ toLoggableString( getNavigablePath() ) + ")";
	}

}
