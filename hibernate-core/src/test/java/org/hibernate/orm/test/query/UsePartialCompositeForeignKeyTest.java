/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.orm.test.query;

import java.util.List;

import org.hibernate.testing.orm.domain.gambit.EntityWithOneToOne;
import org.hibernate.testing.orm.domain.gambit.SimpleEntity;
import org.hibernate.testing.orm.junit.DialectFeatureChecks;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.Jira;
import org.hibernate.testing.orm.junit.RequiresDialectFeature;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import static org.assertj.core.api.Assertions.assertThat;

@SessionFactory
@DomainModel(annotatedClasses = {
		UsePartialCompositeForeignKeyTest.ReferringEntity.class,
		UsePartialCompositeForeignKeyTest.EntityWithCompositeId.class
})
@Jira( "https://hibernate.atlassian.net/browse/HHH-18262" )
public class UsePartialCompositeForeignKeyTest {
	@BeforeAll
	public void setUp(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			EntityWithCompositeId entity = new EntityWithCompositeId();
			entity.id = new EntityWithCompositeIdId();
			entity.id.key1 = 1;
			entity.id.key2 = 1;
			ReferringEntity referringEntity = new ReferringEntity();
			referringEntity.id = 1;
			referringEntity.fk1 = 1;
			session.persist( entity );
			session.persist( referringEntity );
		} );
	}

	@AfterAll
	public void tearDown(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			session.createMutationQuery( "delete from ReferringEntity" ).executeUpdate();
			session.createMutationQuery( "delete from EntityWithCompositeId" ).executeUpdate();
		} );
	}

	@Test
	public void testWhere(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			final List<ReferringEntity> result = session.createQuery(
					"from ReferringEntity e where e.association.id.key1 = 1",
					ReferringEntity.class
			).getResultList();
			assertThat( result.size() ).isEqualTo( 0 );
		} );
	}

	@Entity(name = "ReferringEntity")
	public static class ReferringEntity {
		@Id
		Integer id;
		@Column(name = "fk1")
		Integer fk1;
		@Column(name = "fk2")
		Integer fk2;
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "fk1", referencedColumnName = "key1", insertable = false, updatable = false)
		@JoinColumn(name = "fk2", referencedColumnName = "key2", insertable = false, updatable = false)
		EntityWithCompositeId association;
		String name;
	}

	@Entity(name = "EntityWithCompositeId")
	public static class EntityWithCompositeId {
		@EmbeddedId
		EntityWithCompositeIdId id;
		String name;
	}
	@Embeddable
	public static class EntityWithCompositeIdId {
		Integer key1;
		Integer key2;
	}
}
