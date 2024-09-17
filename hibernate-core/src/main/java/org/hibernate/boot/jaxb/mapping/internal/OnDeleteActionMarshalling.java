/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.boot.jaxb.mapping.internal;

import org.hibernate.annotations.OnDeleteAction;

/**
 * @author Steve Ebersole
 */
public class OnDeleteActionMarshalling {
	public static OnDeleteAction fromXml(String name) {
		return name == null ? null : OnDeleteAction.valueOf( name );
	}

	public static String toXml(OnDeleteAction accessType) {
		return accessType == null ? null : accessType.name();
	}
}