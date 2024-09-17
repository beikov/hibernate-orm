/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.boot.jaxb.mapping.spi.db;

/**
 * @author Steve Ebersole
 */
public interface JaxbColumnSizable extends JaxbColumn {
	Integer getLength();

	default Integer getPrecision() {
		return null;
	}

	default Integer getScale() {
		return null;
	}

	default Integer getSecondPrecision() {
		return null;
	}
}