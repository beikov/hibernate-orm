/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.pl.ast.tree.ProcedureVariable;
import org.hibernate.query.spi.QueryParameterBindings;

/**
 * A context for execution of procedure statements
 */
public interface ProcedureExecutionContext {

	SharedSessionContractImplementor getSession();

	QueryParameterBindings getQueryParameterBindings();

	ProcedureVariableValue getVariableValue(ProcedureVariable variable);

}
