/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import org.hibernate.pl.exec.ProcedureExecutionWalker;
import org.hibernate.sql.exec.spi.JdbcParameterBindings;

/**
 * An executor for procedure operations.
 */
public interface ProcedureExecutor<T> extends ProcedureExecutionWalker {

	T executeProcedure(
			JdbcParameterBindings jdbcParameterBindings,
			ProcedureExecutionContext executionContext);
}
