/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.ast.tree;

import org.hibernate.sql.ast.tree.predicate.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 */
public class ConditionalProcedureStatement implements ProcedureStatement {

	private final Predicate condition;
	private final BlockProcedureStatement thenStatement;
	private final @Nullable BlockProcedureStatement elseStatement;

	public ConditionalProcedureStatement(
			Predicate condition,
			BlockProcedureStatement thenStatement,
			@Nullable BlockProcedureStatement elseStatement) {
		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
	}

	public Predicate getCondition() {
		return condition;
	}

	public BlockProcedureStatement getThenStatement() {
		return thenStatement;
	}

	public BlockProcedureStatement getElseStatement() {
		return elseStatement;
	}
}
