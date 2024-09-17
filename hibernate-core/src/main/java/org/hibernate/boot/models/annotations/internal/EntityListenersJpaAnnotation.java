/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.boot.models.annotations.internal;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.hibernate.models.spi.SourceModelBuildingContext;

import jakarta.persistence.EntityListeners;

@SuppressWarnings({ "ClassExplicitlyAnnotation", "unused" })
@jakarta.annotation.Generated("org.hibernate.orm.build.annotations.ClassGeneratorProcessor")
public class EntityListenersJpaAnnotation implements EntityListeners {
	private java.lang.Class<?>[] value;

	/**
	 * Used in creating dynamic annotation instances (e.g. from XML)
	 */
	public EntityListenersJpaAnnotation(SourceModelBuildingContext modelContext) {
	}

	/**
	 * Used in creating annotation instances from JDK variant
	 */
	public EntityListenersJpaAnnotation(EntityListeners annotation, SourceModelBuildingContext modelContext) {
		this.value = annotation.value();
	}

	/**
	 * Used in creating annotation instances from Jandex variant
	 */
	public EntityListenersJpaAnnotation(Map<String, Object> attributeValues, SourceModelBuildingContext modelContext) {
		this.value = (Class<?>[]) attributeValues.get( "value" );
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return EntityListeners.class;
	}

	@Override
	public java.lang.Class<?>[] value() {
		return value;
	}

	public void value(java.lang.Class<?>[] value) {
		this.value = value;
	}


}