/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html.
 */
package org.hibernate.boot.models.annotations.internal;

import java.lang.annotation.Annotation;

import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.boot.models.HibernateAnnotations;
import org.hibernate.models.spi.SourceModelBuildingContext;

import org.jboss.jandex.AnnotationInstance;

import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJandexValue;

@SuppressWarnings({ "ClassExplicitlyAnnotation", "unused" })
@jakarta.annotation.Generated("org.hibernate.orm.build.annotations.ClassGeneratorProcessor")
public class SelectBeforeUpdateAnnotation implements SelectBeforeUpdate {
	private boolean value;

	/**
	 * Used in creating dynamic annotation instances (e.g. from XML)
	 */
	public SelectBeforeUpdateAnnotation(SourceModelBuildingContext modelContext) {
		this.value = true;
	}

	/**
	 * Used in creating annotation instances from JDK variant
	 */
	public SelectBeforeUpdateAnnotation(SelectBeforeUpdate annotation, SourceModelBuildingContext modelContext) {
		this.value = annotation.value();
	}

	/**
	 * Used in creating annotation instances from Jandex variant
	 */
	public SelectBeforeUpdateAnnotation(AnnotationInstance annotation, SourceModelBuildingContext modelContext) {
		this.value = extractJandexValue( annotation, HibernateAnnotations.SELECT_BEFORE_UPDATE, "value", modelContext );
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return SelectBeforeUpdate.class;
	}

	@Override
	public boolean value() {
		return value;
	}

	public void value(boolean value) {
		this.value = value;
	}


}