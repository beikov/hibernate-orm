/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.ast.tree;

import org.hibernate.sql.ast.tree.Statement;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 */
public class SqlProcedureStatement implements ProcedureStatement {

	private final Statement sqlStatement;
	private final @Nullable ProcedureVariable resultVariable;

	public SqlProcedureStatement(Statement sqlStatement, @Nullable ProcedureVariable resultVariable) {
		this.sqlStatement = sqlStatement;
		this.resultVariable = resultVariable;
	}

	public Statement getSqlStatement() {
		return sqlStatement;
	}

	public ProcedureVariable getResultVariable() {
		return resultVariable;
	}
}
