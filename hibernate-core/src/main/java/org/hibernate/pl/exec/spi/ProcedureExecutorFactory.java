/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.sql.results.spi.ResultsConsumer;
import org.hibernate.sql.results.spi.RowTransformer;

/**
 * A factory for creating an executor for procedure operations.
 */
public interface ProcedureExecutorFactory {

	<R, T> ProcedureExecutor<T> createProcedureExecutor(
			SessionFactoryImplementor sessionFactory,
			ProcedureOperation procedureOperation,
			RowTransformer<R> rowTransformer,
			Class<R> domainResultType,
			ResultsConsumer<T, R> resultsConsumer);



}
