/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class MilvusGeneratedKeysResultSet extends AbstractResultSet<MilvusStatement> {

	private static final List<String> COLUMN_NAMES = List.of( "C0" );
	private final String collectionName;
	private final List<Object> generatedKeys;

	private MilvusResultSetMetaData resultSetMetaData;

	public MilvusGeneratedKeysResultSet(MilvusStatement statement, String collectionName, List<Object> generatedKeys) {
		super(statement);
		this.collectionName = collectionName;
		this.generatedKeys = generatedKeys;
	}

	@Override
	protected int resultSize() {
		return generatedKeys.size();
	}

	@Override
	public void close() throws SQLException {
		resultSetMetaData = null;
		super.close();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		checkClosed();
		if (resultSetMetaData == null) {
			resultSetMetaData = new MilvusResultSetMetaData( collectionName, COLUMN_NAMES );
		}
		return resultSetMetaData;
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		checkClosed();
		return COLUMN_NAMES.indexOf( columnLabel ) + 1;
	}

	@Override
	protected Object getValue(int columnIndex) throws SQLException {
		checkClosed();
		wasNull = false;
		if (columnIndex != 1) {
			throw new SQLException("Column index out of bounds");
		}
		return generatedKeys.get( position );
	}

	@Override
	protected Object getValue(String columnLabel) throws SQLException {
		checkClosed();
		wasNull = false;
		if ( !COLUMN_NAMES.get( 0 ).equals( columnLabel ) ) {
			throw new SQLException("Column not found: " + columnLabel);
		}
		return generatedKeys.get( position );
	}
}
