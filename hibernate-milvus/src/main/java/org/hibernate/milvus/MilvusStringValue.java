/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.milvus;

public record MilvusStringValue(String value) implements MilvusTypedValue {
}
