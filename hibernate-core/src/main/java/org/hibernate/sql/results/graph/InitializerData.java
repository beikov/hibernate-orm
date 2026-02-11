/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.sql.results.graph;


import org.hibernate.sql.results.jdbc.spi.RowProcessingState;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class InitializerData {
	protected final RowProcessingState rowProcessingState;
	protected Initializer.State state = Initializer.State.UNINITIALIZED;
	protected @Nullable Object instance;
	protected Initializer.@Nullable BlockingRunnable<?> blockingRunnable;

	public InitializerData(RowProcessingState rowProcessingState) {
		this.rowProcessingState = rowProcessingState;
	}

	/*
	 * Used by Hibernate Reactive
	 */
	public InitializerData(InitializerData original) {
		requireNonNull( original );
		this.rowProcessingState = original.rowProcessingState;
		this.state = original.state;
		this.instance = original.instance;
		this.blockingRunnable = original.blockingRunnable;
	}

	public RowProcessingState getRowProcessingState() {
		return rowProcessingState;
	}

	public Initializer.State getState() {
		return state;
	}

	public void setState(Initializer.State state) {
		this.state = state;
	}

	public @Nullable Object getInstance() {
		return instance;
	}

	public void setInstance(@Nullable Object instance) {
		this.instance = instance;
	}

	public Initializer.@Nullable BlockingRunnable<?> getBlockingRunnable() {
		return blockingRunnable;
	}

	public void setBlockingRunnable(Initializer.@Nullable BlockingRunnable<?> blockingRunnable) {
		this.blockingRunnable = blockingRunnable;
	}
}
