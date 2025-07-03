/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public record MoLangType(@NotNull String id, @NotNull String name) {

	@NotNull
	private static final Map<String, MoLangType> REGISTRY = new LinkedHashMap<>();

	public MoLangType(@NotNull String id, @NotNull String name) {
		this.id = id;
		this.name = name;
		if (REGISTRY.containsKey(id)) {
			throw new IllegalArgumentException("MoLangType with id '" + id + "' is already registered.");
		}
		REGISTRY.put(id, this);
	}

	public static @NotNull MoLangType byId(@NotNull String pId) {
		return REGISTRY.get(pId);
	}

	public static @NotNull Collection<MoLangType> values() {
		return Collections.unmodifiableCollection(REGISTRY.values());
	}

	@Override
	public @NotNull String toString() {
		return name;
	}

	@NotNull
	public static final MoLangType GENERAL = new MoLangType("g", "general");
	@NotNull
	public static final MoLangType MATH = new MoLangType("m", "math");
	@NotNull
	public static final MoLangType OPERATOR = new MoLangType("o", "operator");
	@NotNull
	public static final MoLangType ENTITY = new MoLangType("e", "entity");
	@NotNull
	public static final MoLangType ANIMATABLE = new MoLangType("q", "animatable");
}
