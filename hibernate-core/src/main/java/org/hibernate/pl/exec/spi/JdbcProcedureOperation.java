/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import org.hibernate.pl.ast.tree.ProcedureVariable;
import org.hibernate.pl.exec.ProcedureExecutionWalker;
import org.hibernate.sql.exec.spi.JdbcOperation;
import org.hibernate.sql.results.spi.ResultsConsumer;
import org.hibernate.sql.results.spi.RowTransformer;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 */
public class JdbcProcedureOperation<T, R> implements ProcedureOperationStatement {

	private final JdbcOperation jdbcOperation;
	private final @Nullable RowTransformer<R> rowTransformer;
	private final @Nullable Class<R> domainResultType;
	private final @Nullable ResultsConsumer<T, R> resultsConsumer;
	private final @Nullable ProcedureVariable resultVariable;

	public JdbcProcedureOperation(JdbcOperation jdbcOperation,
								  @Nullable RowTransformer<R> rowTransformer,
								  @Nullable Class<R> domainResultType,
								  @Nullable ResultsConsumer<T, R> resultsConsumer,
								  @Nullable ProcedureVariable resultVariable) {
		this.jdbcOperation = jdbcOperation;
		this.rowTransformer = rowTransformer;
		this.domainResultType = domainResultType;
		this.resultsConsumer = resultsConsumer;
		this.resultVariable = resultVariable;
	}

	public JdbcOperation getJdbcOperation() {
		return jdbcOperation;
	}

	public RowTransformer<R> getRowTransformer() {
		return rowTransformer;
	}

	public Class<R> getDomainResultType() {
		return domainResultType;
	}

	public ResultsConsumer<T, R> getResultsConsumer() {
		return resultsConsumer;
	}

	public ProcedureVariable getResultVariable() {
		return resultVariable;
	}

	@Override
	public void accept(ProcedureExecutionWalker walker) {
		walker.visitJdbcOperation( this );
	}
}
