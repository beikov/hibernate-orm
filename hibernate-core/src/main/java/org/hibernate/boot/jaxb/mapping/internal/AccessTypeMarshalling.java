/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.boot.jaxb.mapping.internal;

import jakarta.persistence.AccessType;

/**
 * JAXB marshalling for JPA's {@link AccessType}
 *
 * @author Steve Ebersole
 */
public class AccessTypeMarshalling {
	public static AccessType fromXml(String name) {
		return name == null ? null : AccessType.valueOf( name );
	}

	public static String toXml(AccessType accessType) {
		return accessType == null ? null : accessType.name();
	}
}