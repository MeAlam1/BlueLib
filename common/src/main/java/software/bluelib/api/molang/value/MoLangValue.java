/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLangRuntime;

public interface MoLangValue {

	@Nullable
	Object evaluate(@NotNull MoLangRuntime pRuntime);

	static @NotNull MoLangValue fromJson(@NotNull JsonElement pElement) {
		if (!(pElement instanceof JsonPrimitive primitive) || primitive.isBoolean()) {
			throw new IllegalArgumentException("Expected a single value, got: " + pElement.getClass().getSimpleName());
		}

		if (primitive.isNumber()) {
			return new MoLangNumber(primitive.getAsFloat());
		}

		if (primitive.isString()) {
			String value = primitive.getAsString();
			try {
				return new MoLangNumber(Float.parseFloat(value));
			} catch (NumberFormatException e) {
				return new MoLangExpression(value);
			}
		}

		return new MoLangNumber(0f);
	}

	static @NotNull MoLangValue ofNumber(@NotNull Float pValue) {
		return new MoLangNumber(pValue);
	}

	static @NotNull MoLangValue ofExpression(@NotNull String pRaw) {
		return new MoLangExpression(pRaw);
	}
}
