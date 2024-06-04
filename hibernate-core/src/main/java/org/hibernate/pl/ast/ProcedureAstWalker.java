/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.pl.ast;

import org.hibernate.pl.ast.tree.BlockProcedureStatement;
import org.hibernate.pl.ast.tree.ConditionalProcedureStatement;
import org.hibernate.pl.ast.tree.Procedure;
import org.hibernate.pl.ast.tree.ReturnProcedureStatement;
import org.hibernate.pl.ast.tree.SqlProcedureStatement;

/**
 */
public interface ProcedureAstWalker {

	void visitProcedure(Procedure procedure);

	void visitBlockProcedure(BlockProcedureStatement procedure);

	void visitConditionalProcedure(ConditionalProcedureStatement procedure);

	void visitSqlProcedure(SqlProcedureStatement procedure);

	void visitReturnProcedure(ReturnProcedureStatement procedure);

}
