/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.sql.results.graph.entity.internal;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.engine.spi.EntityUniqueKey;
import org.hibernate.metamodel.mapping.internal.ToOneAttributeMapping;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.spi.NavigablePath;
import org.hibernate.sql.results.graph.AssemblerCreationState;
import org.hibernate.sql.results.graph.DomainResult;
import org.hibernate.sql.results.graph.Initializer;
import org.hibernate.sql.results.graph.InitializerParent;

import static org.hibernate.internal.log.LoggingHelper.toLoggableString;

/**
 * @author Andrea Boriero
 */
public class EntitySelectFetchByUniqueKeyInitializer
		extends EntitySelectFetchInitializer<EntitySelectFetchInitializer.EntitySelectFetchInitializerData>
		implements Initializer.LoadByUniqueKeyConsumer<EntitySelectFetchInitializer.EntitySelectFetchInitializerData> {
	private final ToOneAttributeMapping fetchedAttribute;

	public EntitySelectFetchByUniqueKeyInitializer(
			InitializerParent<?> parent,
			ToOneAttributeMapping fetchedAttribute,
			NavigablePath fetchedNavigable,
			EntityPersister concreteDescriptor,
			DomainResult<?> keyResult,
			boolean affectedByFilter,
			AssemblerCreationState creationState) {
		super( parent, fetchedAttribute, fetchedNavigable, concreteDescriptor, keyResult, affectedByFilter, creationState );
		this.fetchedAttribute = fetchedAttribute;
	}

	@Override
	protected void initialize(EntitySelectFetchInitializerData data) {
		final String entityName = concreteDescriptor.getEntityName();
		final String uniqueKeyPropertyName = fetchedAttribute.getReferencedPropertyName();

		final var session = data.getRowProcessingState().getSession();
		final var persistenceContext = session.getPersistenceContextInternal();

		final var entityUniqueKey =
				new EntityUniqueKey(
						entityName,
						uniqueKeyPropertyName,
						data.entityIdentifier,
						concreteDescriptor.getPropertyType( uniqueKeyPropertyName ),
						session.getFactory()
				);
		data.setInstance( persistenceContext.getEntity( entityUniqueKey ) );

		if ( data.getInstance() != null ) {
			data.setInstance( persistenceContext.proxyFor( data.getInstance() ) );
		}
		else {
			final Object instance =
					concreteDescriptor.loadByUniqueKey( uniqueKeyPropertyName, data.entityIdentifier, session );
			postUniqueKeyLoad( data, entityUniqueKey, instance );
		}
	}

	@Override
	protected @Nullable BlockingRunnable<EntitySelectFetchInitializerData> initializeAsync(EntitySelectFetchInitializerData data) {
		final String entityName = concreteDescriptor.getEntityName();
		final String uniqueKeyPropertyName = fetchedAttribute.getReferencedPropertyName();

		final var session = data.getRowProcessingState().getSession();
		final var persistenceContext = session.getPersistenceContextInternal();

		final var entityUniqueKey =
				new EntityUniqueKey(
						entityName,
						uniqueKeyPropertyName,
						data.entityIdentifier,
						concreteDescriptor.getPropertyType( uniqueKeyPropertyName ),
						session.getFactory()
				);
		data.setInstance( persistenceContext.getEntity( entityUniqueKey ) );

		if ( data.getInstance() != null ) {
			data.setInstance( persistenceContext.proxyFor( data.getInstance() ) );
			return null;
		}
		else {
			return new LoadByUniqueKeyBlockingRunnable<>(
					concreteDescriptor,
					entityUniqueKey,
					this
			);
		}
	}

	@Override
	public void postUniqueKeyLoad(EntitySelectFetchInitializerData data, EntityUniqueKey entityUniqueKey, @Nullable Object instance) {
		final var session = data.getRowProcessingState().getSession();
		final var persistenceContext = session.getPersistenceContextInternal();

		data.setState( State.INITIALIZED );
		data.setInstance( instance );
		if ( instance == null ) {
			checkNotFound( data );
		}
		// If the entity was not in the persistence context but was found now,
		// then add it to the persistence context
		persistenceContext.addEntity( entityUniqueKey, instance );
		if ( data.getInstance() != null ) {
			data.setInstance( persistenceContext.proxyFor( data.getInstance() ) );
		}
	}

	@Override
	public String toString() {
		return "EntitySelectFetchByUniqueKeyInitializer("
				+ toLoggableString( getNavigablePath() ) + ")";
	}
}
