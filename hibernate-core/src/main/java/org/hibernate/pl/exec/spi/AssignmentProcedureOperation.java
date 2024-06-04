/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import java.util.function.Function;

import org.hibernate.pl.ast.tree.ProcedureVariable;
import org.hibernate.pl.exec.ProcedureExecutionWalker;

/**
 */
public class AssignmentProcedureOperation implements ProcedureOperationStatement {

	private final ProcedureVariable variable;
	private final Function<ProcedureExecutor<?>, Object> expression;

	public AssignmentProcedureOperation(ProcedureVariable variable, Function<ProcedureExecutor<?>, Object> expression) {
		this.variable = variable;
		this.expression = expression;
	}

	public ProcedureVariable getVariable() {
		return variable;
	}

	public Function<ProcedureExecutor<?>, Object> getExpression() {
		return expression;
	}

	@Override
	public void accept(ProcedureExecutionWalker walker) {
		walker.visitAssignmentOperation( this );
	}
}
