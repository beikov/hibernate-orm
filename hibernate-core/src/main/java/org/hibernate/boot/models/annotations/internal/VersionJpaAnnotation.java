/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html.
 */
package org.hibernate.boot.models.annotations.internal;

import java.lang.annotation.Annotation;

import org.hibernate.models.spi.SourceModelBuildingContext;

import org.jboss.jandex.AnnotationInstance;

import jakarta.persistence.Version;

@SuppressWarnings({ "ClassExplicitlyAnnotation", "unused" })
@jakarta.annotation.Generated("org.hibernate.orm.build.annotations.ClassGeneratorProcessor")
public class VersionJpaAnnotation implements Version {
	/**
	 * Used in creating dynamic annotation instances (e.g. from XML)
	 */
	public VersionJpaAnnotation(SourceModelBuildingContext modelContext) {
	}

	/**
	 * Used in creating annotation instances from JDK variant
	 */
	public VersionJpaAnnotation(Version annotation, SourceModelBuildingContext modelContext) {
	}

	/**
	 * Used in creating annotation instances from Jandex variant
	 */
	public VersionJpaAnnotation(AnnotationInstance annotation, SourceModelBuildingContext modelContext) {
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Version.class;
	}
}