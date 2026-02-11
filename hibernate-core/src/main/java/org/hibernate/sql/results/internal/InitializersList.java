/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.sql.results.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.sql.results.graph.Initializer;
import org.hibernate.sql.results.graph.entity.internal.EntityInitializerImpl;

/**
 * Internal helper to keep track of the various
 * Initializer instances being used during RowReader processing:
 * different types of initializers need to be invoked in different orders,
 * so rather than having to identify the order of initializers again during
 * the processing of each row we keep separated lists of initializers defined
 * upfront and then reuse these sets for the scope of the whole resultset's
 * processing.
 * @author Sanne Grinovero
 */
public final class InitializersList {

	private final Initializer<?>[] initializers;
	private final Initializer<?>[] sortedForResolveInstance;
	private final Initializer<?>[] beforeResolveInstanceInitializers;
	private final Initializer<?>[] afterResolveInstanceInitializers;

	private InitializersList(
			Initializer<?>[] initializers,
			Initializer<?>[] sortedForResolveInstance,
			Initializer<?>[] beforeResolveInstanceInitializers,
			Initializer<?>[] afterResolveInstanceInitializers) {
		this.initializers = initializers;
		this.sortedForResolveInstance = sortedForResolveInstance;
		this.beforeResolveInstanceInitializers = beforeResolveInstanceInitializers;
		this.afterResolveInstanceInitializers = afterResolveInstanceInitializers;
	}

	public Initializer<?>[] getInitializers() {
		return initializers;
	}

	public Initializer<?>[] getSortedForResolveInstance() {
		return sortedForResolveInstance;
	}

	public Initializer<?>[] getBeforeResolveInstanceInitializers() {
		return beforeResolveInstanceInitializers;
	}

	public Initializer<?>[] getAfterResolveInstanceInitializers() {
		return afterResolveInstanceInitializers;
	}

	@Deprecated //for simpler migration to the new SPI
	public List<Initializer<?>> asList() {
		return Arrays.asList( initializers );
	}

	public int size() {
		return initializers.length;
	}

	public static class Builder {
		private final ArrayList<Initializer<?>> initializers;
		int nonCollectionInitializersNum = 0;
		int resolveFirstNum = 0;
		int beforeResolve = 0;
		int afterResolve = 0;

		public Builder() {
			initializers = new ArrayList<>();
		}

		public Builder(int size) {
			initializers = new ArrayList<>( size );
		}

		public void addInitializer(final Initializer<?> initializer) {
			initializers.add( initializer );
			//in this method we perform these checks merely to learn the sizing hints,
			//so to not need dynamically scaling collections.
			//This implies performing both checks twice but since they're cheap it's preferrable
			//to multiple allocations; not least this allows using arrays, which makes iteration
			//cheaper during the row processing - which is very hot.
			if ( !initializer.isCollectionInitializer() ) {
				nonCollectionInitializersNum++;
			}
			switch ( initializer.getAsyncMode() ) {
				case NONE -> {
					if ( initializeFirst( initializer ) ) {
						resolveFirstNum++;
					}
				}
				case BEFORE_RESOLVE -> beforeResolve++;
				case AFTER_RESOLVE -> afterResolve++;
			}
		}

		private static boolean initializeFirst(final Initializer<?> initializer) {
			return initializer instanceof EntityInitializerImpl;
		}

		public InitializersList build() {
			final int size = initializers.size() - beforeResolve - afterResolve;
			final Initializer<?>[] sortedForResolveInstance = new Initializer<?>[size];
			final Initializer<?>[] beforeResolveInstance = new Initializer<?>[beforeResolve];
			final Initializer<?>[] afterResolveInstance = new Initializer<?>[afterResolve];
			int resolveFirstIdx = 0;
			int resolveLaterIdx = resolveFirstNum;
			int beforeResolveIdx = 0;
			int afterResolveIdx = 0;
			final Initializer<?>[] originalSortInitializers = toArray( initializers );
			for ( Initializer<?> initializer : originalSortInitializers ) {
				switch ( initializer.getAsyncMode() ) {
					case NONE -> {
						if ( initializeFirst( initializer ) ) {
							sortedForResolveInstance[resolveFirstIdx++] = initializer;
						}
						else {
							sortedForResolveInstance[resolveLaterIdx++] = initializer;
						}
					}
					case BEFORE_RESOLVE -> beforeResolveInstance[beforeResolveIdx++] = initializer;
					case AFTER_RESOLVE -> afterResolveInstance[afterResolveIdx++] = initializer;
				}
			}
			return new InitializersList(
					originalSortInitializers,
					sortedForResolveInstance,
					beforeResolveInstance,
					afterResolveInstance
			);
		}

		private Initializer<?>[] toArray(final ArrayList<Initializer<?>> initializers) {
			return initializers.toArray( new Initializer<?>[initializers.size()] );
		}

	}

}
