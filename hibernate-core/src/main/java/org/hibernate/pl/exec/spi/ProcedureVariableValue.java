/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import org.hibernate.pl.ast.tree.ProcedureVariable;

/**
 */
public class ProcedureVariableValue {

	private final ProcedureVariable variable;
	private Object value;

	public ProcedureVariableValue(ProcedureVariable variable) {
		this.variable = variable;
	}

	public ProcedureVariable getVariable() {
		return variable;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
