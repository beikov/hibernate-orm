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

import jakarta.persistence.NamedEntityGraph;

import static org.hibernate.boot.models.JpaAnnotations.NAMED_ENTITY_GRAPH;
import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJandexValue;
import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJdkValue;

@SuppressWarnings({ "ClassExplicitlyAnnotation", "unused" })
@jakarta.annotation.Generated("org.hibernate.orm.build.annotations.ClassGeneratorProcessor")
public class NamedEntityGraphJpaAnnotation implements NamedEntityGraph {
	private String name;
	private jakarta.persistence.NamedAttributeNode[] attributeNodes;
	private boolean includeAllAttributes;
	private jakarta.persistence.NamedSubgraph[] subgraphs;
	private jakarta.persistence.NamedSubgraph[] subclassSubgraphs;

	/**
	 * Used in creating dynamic annotation instances (e.g. from XML)
	 */
	public NamedEntityGraphJpaAnnotation(SourceModelBuildingContext modelContext) {
		this.name = "";
		this.attributeNodes = new jakarta.persistence.NamedAttributeNode[0];
		this.includeAllAttributes = false;
		this.subgraphs = new jakarta.persistence.NamedSubgraph[0];
		this.subclassSubgraphs = new jakarta.persistence.NamedSubgraph[0];
	}

	/**
	 * Used in creating annotation instances from JDK variant
	 */
	public NamedEntityGraphJpaAnnotation(NamedEntityGraph annotation, SourceModelBuildingContext modelContext) {
		this.name = annotation.name();
		this.attributeNodes = extractJdkValue( annotation, NAMED_ENTITY_GRAPH, "attributeNodes", modelContext );
		this.includeAllAttributes = annotation.includeAllAttributes();
		this.subgraphs = extractJdkValue( annotation, NAMED_ENTITY_GRAPH, "subgraphs", modelContext );
		this.subclassSubgraphs = extractJdkValue( annotation, NAMED_ENTITY_GRAPH, "subclassSubgraphs", modelContext );
	}

	/**
	 * Used in creating annotation instances from Jandex variant
	 */
	public NamedEntityGraphJpaAnnotation(AnnotationInstance annotation, SourceModelBuildingContext modelContext) {
		this.name = extractJandexValue( annotation, NAMED_ENTITY_GRAPH, "name", modelContext );
		this.attributeNodes = extractJandexValue(
				annotation,
				NAMED_ENTITY_GRAPH,
				"attributeNodes",
				modelContext
		);
		this.includeAllAttributes = extractJandexValue(
				annotation,
				NAMED_ENTITY_GRAPH,
				"includeAllAttributes",
				modelContext
		);
		this.subgraphs = extractJandexValue( annotation, NAMED_ENTITY_GRAPH, "subgraphs", modelContext );
		this.subclassSubgraphs = extractJandexValue(
				annotation,
				NAMED_ENTITY_GRAPH,
				"subclassSubgraphs",
				modelContext
		);
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return NamedEntityGraph.class;
	}

	@Override
	public String name() {
		return name;
	}

	public void name(String value) {
		this.name = value;
	}


	@Override
	public jakarta.persistence.NamedAttributeNode[] attributeNodes() {
		return attributeNodes;
	}

	public void attributeNodes(jakarta.persistence.NamedAttributeNode[] value) {
		this.attributeNodes = value;
	}


	@Override
	public boolean includeAllAttributes() {
		return includeAllAttributes;
	}

	public void includeAllAttributes(boolean value) {
		this.includeAllAttributes = value;
	}


	@Override
	public jakarta.persistence.NamedSubgraph[] subgraphs() {
		return subgraphs;
	}

	public void subgraphs(jakarta.persistence.NamedSubgraph[] value) {
		this.subgraphs = value;
	}


	@Override
	public jakarta.persistence.NamedSubgraph[] subclassSubgraphs() {
		return subclassSubgraphs;
	}

	public void subclassSubgraphs(jakarta.persistence.NamedSubgraph[] value) {
		this.subclassSubgraphs = value;
	}


}