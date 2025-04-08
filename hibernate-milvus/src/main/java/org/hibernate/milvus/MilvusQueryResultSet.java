/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

import io.milvus.v2.service.vector.response.QueryResp;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class MilvusQueryResultSet extends AbstractResultSet<MilvusStatement> {

	private final QueryResp queryResp;
	final String collectionName;
	final List<String> fields;

	private MilvusResultSetMetaData resultSetMetaData;

	public MilvusQueryResultSet(MilvusStatement statement, QueryResp queryResp, String collectionName, List<String> fields) {
		super(statement);
		this.queryResp = queryResp;
		this.collectionName = collectionName;
		this.fields = fields;
	}

	@Override
	protected int resultSize() {
		return queryResp.getQueryResults().size();
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
			resultSetMetaData = new MilvusResultSetMetaData( collectionName, fields );
		}
		return resultSetMetaData;
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		checkClosed();
		return fields.indexOf( columnLabel );
	}

	@Override
	protected Object getValue(int columnIndex) throws SQLException {
		checkClosed();
		wasNull = false;
		String field = fields.get( columnIndex - 1 );
		QueryResp.QueryResult queryResult = queryResp.getQueryResults().get( position );
		return queryResult.getEntity().get( field );
	}

	@Override
	protected Object getValue(String columnLabel) throws SQLException {
		checkClosed();
		wasNull = false;
		int index = fields.indexOf( columnLabel );
		if ( index == -1 ) {
			throw new SQLException("Column not found: " + columnLabel);
		}
		String field = fields.get( index );
		QueryResp.QueryResult queryResult = queryResp.getQueryResults().get( position );
		return queryResult.getEntity().get( field );
	}
}
