/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import java.util.function.Predicate;

import org.hibernate.pl.exec.ProcedureExecutionWalker;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 */
public class ConditionalProcedureOperation implements ProcedureOperationStatement {

	private final Predicate<ProcedureExecutor<?>> condition;
	private final ProcedureOperationStatement thenStatement;
	private final @Nullable ProcedureOperationStatement elseStatement;

	public ConditionalProcedureOperation(
			Predicate<ProcedureExecutor<?>> condition,
			ProcedureOperationStatement thenStatement,
			@Nullable ProcedureOperationStatement elseStatement) {
		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
	}

	public Predicate<ProcedureExecutor<?>> getCondition() {
		return condition;
	}

	public ProcedureOperationStatement getThenStatement() {
		return thenStatement;
	}

	public @Nullable ProcedureOperationStatement getElseStatement() {
		return elseStatement;
	}

	@Override
	public void accept(ProcedureExecutionWalker walker) {
		walker.visitConditionalOperation( this );
	}
}
