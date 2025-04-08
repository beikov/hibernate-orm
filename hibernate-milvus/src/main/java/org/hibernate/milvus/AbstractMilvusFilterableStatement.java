/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

import java.util.Map;

public abstract class AbstractMilvusFilterableStatement extends AbstractMilvusCollectionStatement {
	private String filter;
	private Map<String, MilvusTypedValue> filterTemplateValues;

	public AbstractMilvusFilterableStatement() {
	}

	public int parameterCount() {
		int count = 0;
		if ( filterTemplateValues != null ) {
			for ( Map.Entry<String, MilvusTypedValue> entry : filterTemplateValues.entrySet() ) {
				if ( entry.getValue() == MilvusParameterValue.INSTANCE ) {
					count++;
				}
			}
		}
		return count;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Map<String, MilvusTypedValue> getFilterTemplateValues() {
		return filterTemplateValues;
	}

	public void setFilterTemplateValues(Map<String, MilvusTypedValue> filterTemplateValues) {
		this.filterTemplateValues = filterTemplateValues;
	}
}
