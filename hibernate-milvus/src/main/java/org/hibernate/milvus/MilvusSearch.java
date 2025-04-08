/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

import io.milvus.v2.common.IndexParam;

import java.util.Map;

public final class MilvusSearch extends AbstractMilvusQuery implements MilvusStatementDefinition {
	private String annsField;
	private IndexParam.MetricType metricType;
	private int topK;
	private int roundDecimal;
	private Map<String, Object> searchParams;

	private long guaranteeTimestamp;
	private Long gracefulTime;
	private boolean ignoreGrowing;
	private String groupByFieldName;
	private Integer groupSize;
	private Boolean strictGroupSize;

	public MilvusSearch() {
	}

	public String getAnnsField() {
		return annsField;
	}

	public void setAnnsField(String annsField) {
		this.annsField = annsField;
	}

	public IndexParam.MetricType getMetricType() {
		return metricType;
	}

	public void setMetricType(IndexParam.MetricType metricType) {
		this.metricType = metricType;
	}

	public int getTopK() {
		return topK;
	}

	public void setTopK(int topK) {
		this.topK = topK;
	}

	public int getRoundDecimal() {
		return roundDecimal;
	}

	public void setRoundDecimal(int roundDecimal) {
		this.roundDecimal = roundDecimal;
	}

	public Map<String, Object> getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(Map<String, Object> searchParams) {
		this.searchParams = searchParams;
	}

	public long getGuaranteeTimestamp() {
		return guaranteeTimestamp;
	}

	public void setGuaranteeTimestamp(long guaranteeTimestamp) {
		this.guaranteeTimestamp = guaranteeTimestamp;
	}

	public Long getGracefulTime() {
		return gracefulTime;
	}

	public void setGracefulTime(Long gracefulTime) {
		this.gracefulTime = gracefulTime;
	}

	public boolean isIgnoreGrowing() {
		return ignoreGrowing;
	}

	public void setIgnoreGrowing(boolean ignoreGrowing) {
		this.ignoreGrowing = ignoreGrowing;
	}

	public String getGroupByFieldName() {
		return groupByFieldName;
	}

	public void setGroupByFieldName(String groupByFieldName) {
		this.groupByFieldName = groupByFieldName;
	}

	public Integer getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(Integer groupSize) {
		this.groupSize = groupSize;
	}

	public Boolean getStrictGroupSize() {
		return strictGroupSize;
	}

	public void setStrictGroupSize(Boolean strictGroupSize) {
		this.strictGroupSize = strictGroupSize;
	}
}
