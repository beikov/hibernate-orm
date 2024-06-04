/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import org.hibernate.pl.ast.tree.ProcedureVariable;
import org.hibernate.pl.exec.ProcedureExecutionWalker;

/**
 */
public class ReturnProcedureOperation implements ProcedureOperationStatement {

	private final ProcedureVariable variable;

	public ReturnProcedureOperation(ProcedureVariable variable) {
		this.variable = variable;
	}

	public ProcedureVariable getVariable() {
		return variable;
	}

	@Override
	public void accept(ProcedureExecutionWalker walker) {
		walker.visitReturnOperation( this );
	}
}
