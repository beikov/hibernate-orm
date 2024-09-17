/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.query.criteria;

import org.hibernate.metamodel.model.domain.ManagedDomainType;

/**
 * @author Steve Ebersole
 */
public interface JpaTreatedPath<T,S extends T> extends JpaPath<S> {
	ManagedDomainType<S> getTreatTarget();
}