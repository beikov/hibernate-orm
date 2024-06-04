/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.ast.tree;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 */
public class ReturnProcedureStatement implements ProcedureStatement {

	private final @Nullable ProcedureVariable variable;

	public ReturnProcedureStatement(@Nullable ProcedureVariable variable) {
		this.variable = variable;
	}

	public ProcedureVariable getVariable() {
		return variable;
	}
}
