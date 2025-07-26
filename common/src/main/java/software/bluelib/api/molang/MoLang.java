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
import software.bluelib.api.molang.context.CompositeMoLangContext;
import software.bluelib.api.molang.context.EntityMoLang;

// TODO: Rewrite how all the MoLang is loaded and evaluated.

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

	protected static void registerPrefix(@NotNull String pPrefix, @NotNull MoLangType pType) {
		PREFIX_MAP.put(pPrefix, pType);
	}

	public static @Nullable Object load(@Nullable String pExpression) {
		if (pExpression == null || pExpression.isEmpty()) {
			return null;
		}

		if (pExpression.equals("true") || pExpression.equals("q.true")) {
			return Boolean.TRUE;
		}
		if (pExpression.equals("false") || pExpression.equals("q.false")) {
			return Boolean.FALSE;
		}

		CompositeMoLangContext composite = new CompositeMoLangContext();
		for (MoLangContext ctx : service.getSharedComposite().contexts) {
			composite.addContext(ctx);
		}

		if (pExpression.startsWith("q.")) {
			String expr = pExpression.substring("q.".length());
			return moLangWithContext("q." + expr, MoLangType.ANIMATABLE, composite);
		}

		for (Map.Entry<String, MoLangType> entry : PREFIX_MAP.entrySet()) {
			if (pExpression.startsWith(entry.getKey())) {
				String expr = pExpression.substring(entry.getKey().length());
				expr = entry.getValue().id() + "." + expr;
				return moLangWithContext(expr, entry.getValue(), composite);
			}
		}

		return moLangWithContext(pExpression, MoLangType.GENERAL, composite);
	}

	protected static @Nullable Object moLangWithContext(
			@Nullable String pExpression,
			@NotNull MoLangType pType,
			@NotNull MoLangContext pContext) {
		if (pExpression == null || pExpression.isEmpty()) {
			return null;
		}
		if (pExpression.equals("true") || pExpression.equals("q.true")) {
			return Boolean.TRUE;
		}
		if (pExpression.equals("false") || pExpression.equals("q.false")) {
			return Boolean.FALSE;
		}

		if (pExpression.startsWith("q.")) {
			String expr = pExpression.substring("q.".length());

			for (MoLangType type : MoLangType.values()) {
				MoLangRuntime runtime = service.getRuntimeFor(type);
				runtime.pushContext(type.id(), pContext);
				try {
					Object result = runtime.evaluate(type.id() + "." + expr);
					if (result != null) {
						return result;
					}
				} finally {
					runtime.popContext(type.id());
				}
			}
			return null;
		}

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

	public static @Nullable Object entity(@Nullable String pExpression, @NotNull Entity pEntity) {
		if (pExpression == null || pExpression.isEmpty()) {
			return null;
		}
		CompositeMoLangContext composite = new CompositeMoLangContext();
		for (MoLangContext ctx : service.getSharedComposite().contexts) {
			composite.addContext(ctx);
		}
		composite.addContext(new EntityMoLang(() -> pEntity));
		if (pExpression.startsWith("q.")) {
			return moLangWithContext(pExpression, MoLangType.ANIMATABLE, composite);
		}
		return moLangWithContext(pExpression, MoLangType.ENTITY, composite);
	}
}
