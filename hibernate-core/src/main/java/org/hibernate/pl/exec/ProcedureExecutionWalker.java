/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec;

import org.hibernate.pl.exec.spi.AssignmentProcedureOperation;
import org.hibernate.pl.exec.spi.BlockProcedureOperation;
import org.hibernate.pl.exec.spi.ConditionalProcedureOperation;
import org.hibernate.pl.exec.spi.ProcedureOperation;
import org.hibernate.pl.exec.spi.ReturnProcedureOperation;
import org.hibernate.pl.exec.spi.JdbcProcedureOperation;

/**
 */
public interface ProcedureExecutionWalker {

	void visitProcedure(ProcedureOperation procedure);

	void visitBlockOperation(BlockProcedureOperation block);

	void visitConditionalOperation(ConditionalProcedureOperation conditionalOperation);

	void visitJdbcOperation(JdbcProcedureOperation<?, ?> jdbcOperation);

	void visitReturnOperation(ReturnProcedureOperation returnOperation);

	void visitAssignmentOperation(AssignmentProcedureOperation assignmentOperation);

}
