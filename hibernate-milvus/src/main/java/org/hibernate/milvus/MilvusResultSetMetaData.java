/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class MilvusResultSetMetaData implements ResultSetMetaData {

	private final String collectionName;
	private final List<String> columnNames;

	public MilvusResultSetMetaData(String collectionName, List<String> columnNames) {
		this.collectionName = collectionName;
		this.columnNames = columnNames;
	}

	@Override
	public int getColumnCount() throws SQLException {
		return columnNames.size();
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		return false;
	}

	@Override
	public int isNullable(int column) throws SQLException {
		return 0;
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		return false;
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		return 0;
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		return "";
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		return columnNames.get( column - 1 );
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		return "";
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		return 0;
	}

	@Override
	public int getScale(int column) throws SQLException {
		return 0;
	}

	@Override
	public String getTableName(int column) throws SQLException {
		return collectionName;
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		return "";
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		return 0;
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		return "";
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		return false;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		return "";
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}
