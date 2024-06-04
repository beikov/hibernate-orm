/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.results.graph.entity.internal;

import org.hibernate.engine.FetchTiming;
import org.hibernate.metamodel.mapping.JdbcMapping;
import org.hibernate.metamodel.mapping.SelectableMapping;
import org.hibernate.metamodel.mapping.internal.ToOneAttributeMapping;
import org.hibernate.spi.NavigablePath;
import org.hibernate.sql.ast.spi.SqlAstCreationState;
import org.hibernate.sql.ast.spi.SqlExpressionResolver;
import org.hibernate.sql.ast.spi.SqlSelection;
import org.hibernate.sql.ast.tree.expression.Expression;
import org.hibernate.sql.results.graph.AssemblerCreationState;
import org.hibernate.sql.results.graph.DomainResult;
import org.hibernate.sql.results.graph.DomainResultCreationState;
import org.hibernate.sql.results.graph.FetchParent;
import org.hibernate.sql.results.graph.InitializerParent;
import org.hibernate.sql.results.graph.basic.BasicResult;
import org.hibernate.sql.results.graph.basic.BasicResultAssembler;
import org.hibernate.sql.results.graph.entity.EntityInitializer;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author Andrea Boriero
 * @author Steve Ebersole
 */
public class EntityDelayedFetchImpl extends AbstractNonJoinedEntityFetch {

	private final @Nullable BasicResult<?> targetIdResultPart;

	public EntityDelayedFetchImpl(
			FetchParent fetchParent,
			ToOneAttributeMapping fetchedAttribute,
			NavigablePath navigablePath,
			DomainResult<?> keyResult,
			boolean selectByUniqueKey,
			DomainResultCreationState creationState) {
		super(
				navigablePath,
				fetchedAttribute,
				fetchParent,
				keyResult,
				fetchedAttribute.getEntityMappingType().getEntityPersister().isConcreteProxy(),
				selectByUniqueKey,
				creationState
		);
		final SqlAstCreationState sqlAstCreationState = creationState.getSqlAstCreationState();
		final boolean affectedByFilters = fetchedAttribute.getAssociatedEntityMappingType().isAffectedByEnabledFilters(
				sqlAstCreationState.getLoadQueryInfluencers(),
				sqlAstCreationState.applyOnlyLoadByKeyFilters()
		);
		if ( affectedByFilters ) {
			final SelectableMapping firstSelectableMapping = fetchedAttribute.getAssociatedEntityMappingType()
					.getIdentifierMapping()
					.getSelectable( 0 );
			final SqlExpressionResolver sqlExpressionResolver = sqlAstCreationState.getSqlExpressionResolver();
			final Expression targetIdResultPartExpression = sqlExpressionResolver.resolveSqlExpression(
					sqlAstCreationState.getFromClauseAccess().getTableGroup( navigablePath )
							.resolveTableReference( firstSelectableMapping.getContainingTableExpression() ),
					firstSelectableMapping
			);
			final SqlSelection targetIdResultPartSelection = sqlExpressionResolver.resolveSqlSelection(
					targetIdResultPartExpression,
					firstSelectableMapping.getJdbcMapping().getJdbcJavaType(),
					this,
					sqlAstCreationState.getCreationContext().getSessionFactory().getTypeConfiguration()
			);

			targetIdResultPart = new BasicResult<>(
					targetIdResultPartSelection.getValuesArrayPosition(),
					null,
					firstSelectableMapping.getJdbcMapping(),
					navigablePath,
					false,
					!targetIdResultPartSelection.isVirtual()
			);
		}
		else {
			targetIdResultPart = null;
		}
	}

	@Override
	public FetchTiming getTiming() {
		return FetchTiming.DELAYED;
	}

	@Override
	public EntityInitializer createInitializer(InitializerParent parent, AssemblerCreationState creationState) {
		return new EntityDelayedFetchInitializer(
				parent,
				getNavigablePath(),
				getEntityValuedModelPart(),
				isSelectByUniqueKey(),
				getKeyResult().createResultAssembler( parent, creationState ),
				getDiscriminatorFetch() != null
						? (BasicResultAssembler<?>) getDiscriminatorFetch().createResultAssembler( parent, creationState )
						: null,
				targetIdResultPart == null ? null : targetIdResultPart.createResultAssembler( parent, creationState )
		);
	}
}
