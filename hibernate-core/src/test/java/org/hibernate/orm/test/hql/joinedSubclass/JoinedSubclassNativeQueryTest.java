/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.hql.joinedSubclass;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.mapping.internal.SqlTypedMappingImpl;

import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.Jira;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.type.spi.TypeConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jan Schatteman
 */
@DomainModel(
		annotatedClasses = {
				JoinedSubclassNativeQueryTest.Person.class,
				JoinedSubclassNativeQueryTest.Employee.class
		}
)
@SessionFactory
public class JoinedSubclassNativeQueryTest {

	@BeforeAll
	public void setup(SessionFactoryScope scope) {
		scope.inTransaction(
				session -> {
					Person p = new Person();
					p.setId( 1L );
					p.setFirstName( "Jan" );
					session.persist( p );
					Employee p2 = new Employee();
					p2.setId( 2L );
					p2.setFirstName( "Christian" );
					p2.setCompanyName( "CommonHaus" );
					session.persist( p2 );
				}
		);
	}

	@AfterAll
	public void tearDown(SessionFactoryScope scope) {
		scope.inTransaction(
				session -> session.createMutationQuery( "delete from Person" ).executeUpdate()
		);
	}

	@Test
	@Jira("https://hibernate.atlassian.net/browse/HHH-19569")
	public void testJoinedInheritanceAliasInjection(SessionFactoryScope scope) {
		scope.inTransaction(
				session -> {
					Person p = session.createNativeQuery( "select {p.*} from Person p left join Employee p_1_ where p.id = 2", Person.class )
							.addEntity( "p", Person.class )
							.getSingleResult();
					assertNotNull( p );
					assertInstanceOf( Employee.class, p );
					assertEquals( "Christian", p.getFirstName() );
					assertEquals( "CommonHaus", ((Employee) p).getCompanyName() );
				}
		);
	}

	@Test
	@Jira("https://hibernate.atlassian.net/browse/HHH-16180")
	public void testJoinedInheritanceNativeQuery(SessionFactoryScope scope) {
		scope.inTransaction(
				session -> {
					final SessionFactoryImplementor sessionFactory = scope.getSessionFactory();
					final TypeConfiguration typeConfiguration = sessionFactory.getTypeConfiguration();
					final String nullColumnString = sessionFactory
							.getJdbcServices()
							.getDialect()
							.getSelectClauseNullString(
									new SqlTypedMappingImpl( typeConfiguration.getBasicTypeForJavaType( String.class ) ),
									typeConfiguration
							);
					// PostgreSQLDialect#getSelectClauseNullString produces e.g. `null::text` which we interpret as parameter,
					// so workaround this problem by configuring to ignore JDBC parameters
					session.setProperty( AvailableSettings.NATIVE_IGNORE_JDBC_PARAMETERS, true );
					Person p = session.createNativeQuery( "select p.*, " + nullColumnString + " as company_name, 0 as clazz_  from Person p where p.id = 1", Person.class ).getSingleResult();
					assertNotNull( p );
					assertEquals( "Jan", p.getFirstName() );
				}
		);
	}

	@Entity(name = "Person")
	@Inheritance(strategy = InheritanceType.JOINED)
	public static class Person {
		@Id
		private Long id;

		@Basic(optional = false)
		@Column(name = "first_name")
		private String firstName;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
	}

	@Entity(name = "Employee")
	public static class Employee extends Person {
		@Basic(optional = false)
		@Column(name = "company_name")
		private String companyName;

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
	}

}
