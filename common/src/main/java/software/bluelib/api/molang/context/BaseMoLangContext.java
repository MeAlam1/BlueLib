/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLangContext;
import software.bluelib.api.molang.MoLangRuntime;

public abstract class BaseMoLangContext implements MoLangContext {

	@NotNull
	protected final Map<String, Object> variables = new HashMap<>();
	@NotNull
	protected final Map<String, BiFunction<List<Object>, MoLangRuntime, Object>> functions = new HashMap<>();

	public void registerFunction(@NotNull String pName, @Nullable BiFunction<List<Object>, MoLangRuntime, Object> pFunction) {
		functions.put(pName, pFunction);
	}

	public void setVariable(@NotNull String pName, @Nullable Supplier<?> pSupplier) {
		variables.put(pName, pSupplier);
	}

	public void setVariable(@NotNull String pName, @Nullable Object pSupplier) {
		variables.put(pName, pSupplier instanceof Supplier<?> ? pSupplier : (Supplier<?>) () -> pSupplier);
	}

	@Override
	public @Nullable Object getVariable(@NotNull String pName) {
		Supplier<?> supplier = (Supplier<?>) variables.get(pName);
		return supplier != null ? supplier.get() : null;
	}

	@Override
	public @Nullable Object callFunction(@NotNull String pName, @Nullable List<Object> pArguments) {
		var fn = functions.get(pName);
		return fn != null ? fn.apply(pArguments, null) : null;
	}
}
