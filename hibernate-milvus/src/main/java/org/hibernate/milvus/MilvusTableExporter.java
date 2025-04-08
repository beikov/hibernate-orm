/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

import io.milvus.v2.common.DataType;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.Exporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MilvusTableExporter implements Exporter<Table> {

	public static final MilvusTableExporter INSTANCE = new MilvusTableExporter();

	@Override
	public String[] getSqlCreateStrings(Table exportable, Metadata metadata, SqlStringGenerationContext context) {
		String collectionName = exportable.getQualifiedTableName().getTableName().toString();
		Collection<Column> columns = exportable.getColumns();

		List<MilvusCreateCollection.FieldSchema> fields = new ArrayList<>( columns.size() );

		for ( Column column : columns ) {
			boolean primaryKey = exportable.isPrimaryKey( column );
			String sqlType = column.getSqlType( metadata );
			DataType dataType;
			DataType elementType = null;
			if ( sqlType.endsWith( " array" ) ) {
				dataType = DataType.Array;
				elementType = determineType( sqlType );
			}
			else {
				dataType = determineType( sqlType );
				if ( primaryKey ) {
					dataType = switch ( dataType ) {
						// Milvus requires Int64 for the primary key
						case Int8, Int16, Int32 -> DataType.Int64;
						default -> dataType;
					};
				}
			}
			MilvusCreateCollection.FieldSchema field = new MilvusCreateCollection.FieldSchema(
					column.getName(),
					null,
					dataType,
					column.getLength() == null ? null : column.getLength().intValue(),
					column.getArrayLength(),
					primaryKey,
					false,
					false,
					column.isIdentity(),
					elementType,
					null,
					column.isNullable(),
					column.getDefaultValue(),
					false,
					null,
					null
			);
			fields.add( field );
		}

		MilvusCreateCollection.Schema schema = new MilvusCreateCollection.Schema( fields );
		MilvusCreateCollection collection = new MilvusCreateCollection(
				collectionName,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				schema,
				null,
				null,
				null,
				null
		);
		return new String[] {MilvusJsonHelper.serializeDefinition( collection )};
	}

	private DataType determineType(String sqlType) {
		return switch ( sqlType ) {
			case "string", "varchar" -> DataType.VarChar;
			case "boolean" -> DataType.Bool;
			case "tinyint" -> DataType.Int8;
			case "smallint" -> DataType.Int16;
			case "integer" -> DataType.Int32;
			case "bigint" -> DataType.Int64;
			case "float", "real" -> DataType.Float;
			case "double precision" -> DataType.Double;
			case "json" -> DataType.JSON;
			case "binary_vector" -> DataType.BinaryVector;
			case "float_vector" -> DataType.FloatVector;
			case "float16_vector" -> DataType.Float16Vector;
			case "bfloat16_vector" -> DataType.BFloat16Vector;
			case "sparse_float_vector" -> DataType.SparseFloatVector;
			default -> throw new IllegalArgumentException( "Unsupported sql type: " + sqlType );
		};
	}

	@Override
	public String[] getSqlDropStrings(Table exportable, Metadata metadata, SqlStringGenerationContext context) {
		String collectionName = exportable.getQualifiedTableName().getTableName().toString();
		MilvusDropCollection collection = new MilvusDropCollection(
				collectionName,
				null,
				null
		);
		return new String[] {MilvusJsonHelper.serializeDefinition( collection )};
	}
}
