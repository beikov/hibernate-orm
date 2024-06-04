/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate;

import java.util.Locale;

import org.hibernate.annotations.FilterDef;

import jakarta.persistence.EntityNotFoundException;

/**
 * Exception for associations that would be set to null when applying a {@link Filter}.
 *
 * @see FilterDef#applyToLoadByKey()
 */
public class EntityFilterException extends EntityNotFoundException {
	private final String entityName;
	private final Object identifier;

	public EntityFilterException(String entityName, Object identifier, String role) {
		super(
				String.format(
						Locale.ROOT,
						"Entity `%s` with identifier value `%s` is filtered for association `%s`",
						entityName,
						identifier,
						role
				)
		);
		this.entityName = entityName;
		this.identifier = identifier;
	}

	public String getEntityName() {
		return entityName;
	}

	public Object getIdentifier() {
		return identifier;
	}
}
