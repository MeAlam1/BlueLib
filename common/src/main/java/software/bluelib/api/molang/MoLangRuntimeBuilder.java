/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.context.BaseMoLangContext;
import software.bluelib.api.molang.registry.MoLangContextRegistry;

public class MoLangRuntimeBuilder {

	public static class Input {

		private final Map<String, Object> references = new HashMap<>();

		public @Nullable Object get(String pKey) {
			return references.get(pKey);
		}

		public <T> @Nullable T get(String pKey, Class<T> pClazz) {
			Object obj = references.get(pKey);
			return pClazz.isInstance(obj) ? pClazz.cast(obj) : null;
		}

		public @NotNull Map<String, Object> all() {
			return references;
		}
	}

	private final Input input = new Input();

	public MoLangRuntimeBuilder with(@NotNull String pKey, @NotNull Object pValue) {
		input.references.put(pKey, pValue);
		return this;
	}

	public @NotNull MoLangRuntime build() {
		List<BaseMoLangContext> contexts = MoLangContextRegistry.createContexts(input);
		return new MoLangRuntime(contexts);
	}
}
