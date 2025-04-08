/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.SqlAstTranslatorFactory;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.spi.StandardSqlAstTranslatorFactory;
import org.hibernate.sql.ast.tree.Statement;
import org.hibernate.sql.exec.spi.JdbcOperation;
import org.hibernate.tool.schema.spi.Exporter;
import org.hibernate.type.BasicArrayType;
import org.hibernate.type.BasicType;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.java.spi.JavaTypeRegistry;
import org.hibernate.type.descriptor.jdbc.ArrayJdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;
import org.hibernate.type.descriptor.sql.internal.DdlTypeImpl;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;
import org.hibernate.type.spi.TypeConfiguration;

import java.lang.reflect.Type;

import static org.hibernate.type.SqlTypes.CHAR;
import static org.hibernate.type.SqlTypes.CLOB;
import static org.hibernate.type.SqlTypes.NCHAR;
import static org.hibernate.type.SqlTypes.NCLOB;
import static org.hibernate.type.SqlTypes.NVARCHAR;
import static org.hibernate.type.SqlTypes.VARCHAR;

/**
 * An SQL dialect for Milvus.
 */
public class MilvusDialect extends Dialect {

	private static final DatabaseVersion MINIMUM_VERSION = DatabaseVersion.make( 2, 5 );
	private static final Type[] VECTOR_JAVA_TYPES = {
			Float[].class,
			float[].class
	};

	@SuppressWarnings("unused")
	public MilvusDialect() {
		this( MINIMUM_VERSION );
	}

	public MilvusDialect(DialectResolutionInfo info) {
		this( info.makeCopyOrDefault( MINIMUM_VERSION ) );
		registerKeywords( info );
	}

	public MilvusDialect(DatabaseVersion version) {
		super( version );
	}

	@Override
	public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		super.contributeTypes( typeContributions, serviceRegistry );

		final TypeConfiguration typeConfiguration = typeContributions.getTypeConfiguration();
		final JavaTypeRegistry javaTypeRegistry = typeConfiguration.getJavaTypeRegistry();
		final JdbcTypeRegistry jdbcTypeRegistry = typeConfiguration.getJdbcTypeRegistry();
		final DdlTypeRegistry ddlTypeRegistry = typeConfiguration.getDdlTypeRegistry();
		final BasicTypeRegistry basicTypeRegistry = typeConfiguration.getBasicTypeRegistry();
		final BasicType<Float> floatBasicType = basicTypeRegistry.resolve( StandardBasicTypes.FLOAT );
		final ArrayJdbcType vectorJdbcType = new BinaryVectorJdbcType( jdbcTypeRegistry.getDescriptor( SqlTypes.FLOAT ) );
		jdbcTypeRegistry.addDescriptor( SqlTypes.VECTOR, vectorJdbcType );
		for ( Type vectorJavaType : VECTOR_JAVA_TYPES ) {
			basicTypeRegistry.register(
					new BasicArrayType<>(
							floatBasicType,
							vectorJdbcType,
							javaTypeRegistry.getDescriptor( vectorJavaType )
					),
					StandardBasicTypes.VECTOR.getName()
			);
		}
		ddlTypeRegistry.addDescriptor(
				new DdlTypeImpl( SqlTypes.VECTOR, "float_vector", this )
		);
	}

	@Override
	protected String columnType(int sqlTypeCode) {
		return switch ( sqlTypeCode ) {
			case CHAR,VARCHAR, NCHAR, NVARCHAR, CLOB, NCLOB -> "varchar";
			default -> super.columnType( sqlTypeCode );
		};
	}

	@Override
	public void appendLiteral(SqlAppender appender, String literal) {
		appender.appendDoubleQuoteEscapedString( literal );
	}

	@Override
	public Exporter<Table> getTableExporter() {
		return MilvusTableExporter.INSTANCE;
	}

	@Override
	public SqlAstTranslatorFactory getSqlAstTranslatorFactory() {
		return new StandardSqlAstTranslatorFactory() {
			@Override
			protected <T extends JdbcOperation> SqlAstTranslator<T> buildTranslator(
					SessionFactoryImplementor sessionFactory, Statement statement) {
				return new MilvusSqlAstTranslator<>( sessionFactory, statement );
			}
		};
	}
}
