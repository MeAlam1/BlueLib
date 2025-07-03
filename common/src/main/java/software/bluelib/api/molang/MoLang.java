/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.context.EntityMoLang;

public class MoLang {

	@NotNull
	public static final MoLangService service = new MoLangService();

	@NotNull
	private static final Map<String, MoLangType> PREFIX_MAP = new HashMap<>();

	static {
		for (MoLangType type : MoLangType.values()) {
			PREFIX_MAP.put(type.id() + ".", type);
		}
	}

	public static void registerPrefix(@NotNull String pPrefix, @NotNull MoLangType pType) {
		PREFIX_MAP.put(pPrefix, pType);
	}

	public static @Nullable Object autoMoLang(@NotNull String pExpression) {
		for (Map.Entry<String, MoLangType> entry : PREFIX_MAP.entrySet()) {
			if (pExpression.startsWith(entry.getKey())) {
				String expr = pExpression.substring(entry.getKey().length());
				expr = entry.getValue().id() + "." + expr;
				return service.getRuntimeFor(entry.getValue()).evaluate(expr);
			}
		}
		return service.getRuntimeFor(MoLangType.GENERAL).evaluate(pExpression);
	}

	public static @Nullable Object moLangWithContext(@NotNull String pExpression, @NotNull MoLangType pType, @NotNull MoLangContext pContext) {
		MoLangRuntime runtime = service.getRuntimeFor(pType);
		String prefix = pType.id() + ".";

		runtime.pushContext(pType.id(), pContext);
		try {
			if (pExpression.startsWith(prefix)) {
				return runtime.evaluate(pExpression);
			}
			String transformed = prefix + pExpression;
			return runtime.evaluate(transformed);
		} finally {
			runtime.popContext(pType.id());
		}
	}

	public static @Nullable Object moLangEntity(@NotNull String pExpression, @NotNull Entity pEntity) {
		return moLangWithContext(pExpression, MoLangType.ENTITY, new EntityMoLang(() -> pEntity));
	}
}
