/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.boot.jaxb.mapping.internal;

import org.hibernate.annotations.NotFoundAction;

/**
 * @author Steve Ebersole
 */
public class NotFoundActionMarshalling {
	public static NotFoundAction fromXml(String name) {
		return name == null ? null : NotFoundAction.valueOf( name );
	}

	public static String toXml(NotFoundAction action) {
		return action == null ? null : action.name();
	}
}