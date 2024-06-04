/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.exec.spi;

import java.util.List;

import org.hibernate.pl.exec.ProcedureExecutionWalker;

/**
 */
public class BlockProcedureOperation implements ProcedureOperationStatement {

	private final List<ProcedureOperationStatement> statements;

	public BlockProcedureOperation(List<ProcedureOperationStatement> statements) {
		this.statements = statements;
	}

	public List<ProcedureOperationStatement> getStatements() {
		return statements;
	}

	@Override
	public void accept(ProcedureExecutionWalker walker) {
		walker.visitBlockOperation( this );
	}
}
