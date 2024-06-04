/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.internal;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.pl.exec.spi.ProcedureExecutor;
import org.hibernate.pl.exec.spi.ProcedureExecutorFactory;
import org.hibernate.pl.exec.spi.ProcedureOperation;
import org.hibernate.sql.results.spi.ResultsConsumer;
import org.hibernate.sql.results.spi.RowTransformer;

/**
 * Standard ProcedureExecutor implementation used by Hibernate,
 * through {@link ProcedureExecutorFactoryStandardImpl#INSTANCE}
 */
public class ProcedureExecutorFactoryStandardImpl implements ProcedureExecutorFactory {
	/**
	 * Singleton access
	 */
	public static final ProcedureExecutorFactoryStandardImpl INSTANCE = new ProcedureExecutorFactoryStandardImpl();

	@Override
	public <R, T> ProcedureExecutor<T> createProcedureExecutor(
			SessionFactoryImplementor sessionFactory,
			ProcedureOperation procedureOperation,
			RowTransformer<R> rowTransformer,
			Class<R> domainResultType,
			ResultsConsumer<T, R> resultsConsumer) {
		return new ProcedureExecutorStandardImpl<>(
				procedureOperation,
				rowTransformer,
				domainResultType,
				resultsConsumer
		);
	}
}
