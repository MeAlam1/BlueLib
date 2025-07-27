/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.context.*;
import software.bluelib.oldLoader.animation.AnimationState;

public class MoLangContextRegistry {

	public static void init() {
		MoLangContextRegistry.register(input -> new GeneralMoLang());
		MoLangContextRegistry.register(input -> new MathMoLang());
		MoLangContextRegistry.register(input -> new OperatorMoLang());

		MoLangContextRegistry.register(input -> {
			Supplier<?> supplier = input.get("bluelib_state", Supplier.class);
			if (supplier != null) {
				Object value = supplier.get();
				if (value instanceof AnimationState<?> animationState) {
					return new AnimatableMoLang(animationState);
				}
			}
			return null;
		});

		MoLangContextRegistry.register(input -> {
			Supplier<?> supplier = input.get("bluelib_entity", Supplier.class);
			if (supplier instanceof Supplier<?>) {
				Object obj = supplier.get();
				if (obj instanceof Entity entity) {
					for (var factory : CUSTOM_ENTITY_CONTEXT_FACTORIES) {
						BaseMoLangContext ctx = factory.apply(entity);
						if (ctx != null) return ctx;
					}
					return new EntityMoLang(() -> entity);
				}
			}
			return null;
		});
	}

	private static final List<Function<MoLangRuntimeBuilder.Input, BaseMoLangContext>> CONTEXT_SUPPLIERS = new ArrayList<>();

	private static final List<Function<Entity, ? extends BaseMoLangContext>> CUSTOM_ENTITY_CONTEXT_FACTORIES = new ArrayList<>();

	public static void registerCustomEntityContext(Function<Entity, ? extends BaseMoLangContext> factory) {
		CUSTOM_ENTITY_CONTEXT_FACTORIES.add(factory);
	}

	public static void register(@NotNull Function<MoLangRuntimeBuilder.Input, @Nullable BaseMoLangContext> pFactory) {
		CONTEXT_SUPPLIERS.add(pFactory);
	}

	static @NotNull List<BaseMoLangContext> createContexts(MoLangRuntimeBuilder.Input pInput) {
		List<BaseMoLangContext> result = new ArrayList<>();
		for (var fn : CONTEXT_SUPPLIERS) {
			BaseMoLangContext ctx = fn.apply(pInput);
			if (ctx != null) result.add(ctx);
		}
		return result;
	}
}
