/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.internal;

import org.hibernate.HibernateException;
import org.hibernate.pl.ast.tree.ProcedureVariable;
import org.hibernate.pl.exec.spi.AssignmentProcedureOperation;
import org.hibernate.pl.exec.spi.BlockProcedureOperation;
import org.hibernate.pl.exec.spi.ConditionalProcedureOperation;
import org.hibernate.pl.exec.spi.ProcedureExecutionContext;
import org.hibernate.pl.exec.spi.ProcedureExecutor;
import org.hibernate.pl.exec.spi.ProcedureOperation;
import org.hibernate.pl.exec.spi.ProcedureOperationStatement;
import org.hibernate.pl.exec.spi.ProcedureVariableValue;
import org.hibernate.pl.exec.spi.ReturnProcedureOperation;
import org.hibernate.pl.exec.spi.JdbcProcedureOperation;
import org.hibernate.sql.exec.internal.BaseExecutionContext;
import org.hibernate.sql.exec.spi.ExecutionContext;
import org.hibernate.sql.exec.spi.JdbcOperation;
import org.hibernate.sql.exec.spi.JdbcOperationQueryMutation;
import org.hibernate.sql.exec.spi.JdbcOperationQuerySelect;
import org.hibernate.sql.exec.spi.JdbcParameterBindings;
import org.hibernate.sql.results.spi.ResultsConsumer;
import org.hibernate.sql.results.spi.RowTransformer;

/**
 * Standard ProcedureExecutor implementation used by Hibernate
 */
public class ProcedureExecutorStandardImpl<T, R> implements ProcedureExecutor<T> {
	private final ProcedureOperation procedureOperation;
//	private final RowTransformer<R> rowTransformer;
//	private final Class<R> domainResultType;
//	private final ResultsConsumer<T, R> resultsConsumer;

	private JdbcParameterBindings jdbcParameterBindings;
	private ProcedureExecutionContext executionContext;
	private ProcedureVariableValue resultValue;

	public ProcedureExecutorStandardImpl(
			ProcedureOperation procedureOperation,
			RowTransformer<R> rowTransformer,
			Class<R> domainResultType,
			ResultsConsumer<T, R> resultsConsumer) {
		this.procedureOperation = procedureOperation;
//		this.rowTransformer = rowTransformer;
//		this.domainResultType = domainResultType;
//		this.resultsConsumer = resultsConsumer;
	}

	@Override
	public T executeProcedure(JdbcParameterBindings jdbcParameterBindings, ProcedureExecutionContext executionContext) {
		this.jdbcParameterBindings = jdbcParameterBindings;
		this.executionContext = executionContext;
		try {
			visitProcedure( procedureOperation );
		}
		catch (RuntimeException ex) {
			throw new HibernateException( "Error during procedure execution", ex );
		}
		return (T) resultValue.getValue();
	}

	@Override
	public void visitProcedure(ProcedureOperation procedure) {
		for ( ProcedureOperationStatement statement : procedure.getStatements() ) {
			if ( resultValue != null ) {
				break;
			}
			statement.accept( this );
		}
	}

	@Override
	public void visitBlockOperation(BlockProcedureOperation block) {
		for ( ProcedureOperationStatement statement : block.getStatements() ) {
			statement.accept( this );
		}
	}

	@Override
	public void visitConditionalOperation(ConditionalProcedureOperation conditionalOperation) {
		if ( conditionalOperation.getCondition().test( this ) ) {
			conditionalOperation.getThenStatement().accept( this );
		}
		else if ( conditionalOperation.getElseStatement() != null ) {
			conditionalOperation.getElseStatement().accept( this );
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void visitJdbcOperation(JdbcProcedureOperation<?, ?> jdbcProcedureOperation) {
		final JdbcOperation jdbcOperation = jdbcProcedureOperation.getJdbcOperation();
		final ProcedureVariable resultVariable = jdbcProcedureOperation.getResultVariable();
		final ExecutionContext jdbcExecutionContext = new BaseExecutionContext( executionContext.getSession() );
		if ( jdbcOperation instanceof JdbcOperationQuerySelect ) {
			final RowTransformer<Object> rowTransformer = (RowTransformer<Object>) jdbcProcedureOperation.getRowTransformer();
			final Class<Object> domainResultType = (Class<Object>) jdbcProcedureOperation.getDomainResultType();
			final ResultsConsumer<Object, Object> resultsConsumer = (ResultsConsumer<Object, Object>) jdbcProcedureOperation.getResultsConsumer();
			final ProcedureVariableValue variableValue = executionContext.getVariableValue( resultVariable );
			final Object result = executionContext.getSession()
					.getJdbcServices()
					.getJdbcSelectExecutor()
					.executeQuery(
							(JdbcOperationQuerySelect) jdbcOperation,
							jdbcParameterBindings,
							jdbcExecutionContext,
							rowTransformer,
							domainResultType,
							sqlString -> executionContext.getSession()
									.getJdbcCoordinator()
									.getStatementPreparer()
									.prepareQueryStatement( sqlString, false, null ),
							resultsConsumer
					);
			variableValue.setValue( result );
		}
		else {
			assert jdbcOperation instanceof JdbcOperationQueryMutation;
			final int updatedCount = executionContext.getSession()
					.getJdbcServices()
					.getJdbcMutationExecutor()
					.execute(
							(JdbcOperationQueryMutation) jdbcOperation,
							jdbcParameterBindings,
							sql -> executionContext.getSession()
									.getJdbcCoordinator()
									.getStatementPreparer()
									.prepareStatement( sql ),
							(integer, preparedStatement) -> {},
							jdbcExecutionContext
					);
			if ( resultVariable != null ) {
				final ProcedureVariableValue variableValue = executionContext.getVariableValue( resultVariable );
				variableValue.setValue( updatedCount );
			}
		}
	}

	@Override
	public void visitReturnOperation(ReturnProcedureOperation returnOperation) {
		resultValue = executionContext.getVariableValue( returnOperation.getVariable() );
	}

	@Override
	public void visitAssignmentOperation(AssignmentProcedureOperation assignmentOperation) {
		final ProcedureVariableValue variableValue = executionContext.getVariableValue( assignmentOperation.getVariable() );
		variableValue.setValue( assignmentOperation.getExpression().apply( this ) );
	}
}
