/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLangRuntimeBuilder;
import software.bluelib.api.molang.context.*;
import software.bluelib.api.molang.context.entity.EntityMoLang;
import software.bluelib.api.molang.context.math.AdvancedMathMoLang;
import software.bluelib.api.molang.context.math.BasicMathMoLang;
import software.bluelib.api.molang.context.math.RandomMoLang;
import software.bluelib.api.molang.context.math.TrigMoLang;
import software.bluelib.loader.animation.AnimationState;

public class MoLangContextRegistry {

	public static void init() {
		MoLangContextRegistry.register(input -> new GeneralMoLang());
		MoLangContextRegistry.register(input -> new BasicMathMoLang());
		MoLangContextRegistry.register(input -> new AdvancedMathMoLang());
		MoLangContextRegistry.register(input -> new RandomMoLang());
		MoLangContextRegistry.register(input -> new TrigMoLang());
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

		MoLangEntityRegistry.init();

		MoLangContextRegistry.register(input -> {
			Supplier<?> supplier = input.get("bluelib_entity", Supplier.class);
			if (supplier != null) {
				Object obj = supplier.get();
				if (obj instanceof Entity entity) {
					List<BaseMoLangContext> contexts = new ArrayList<>();
					for (var factory : ENTITY_CONTEXT_FACTORIES) {
						BaseMoLangContext ctx = factory.apply(entity);
						if (ctx != null) contexts.add(ctx);
					}
					if (contexts.isEmpty()) {
						contexts.add(new EntityMoLang(() -> entity));
					}
					return new MultiMoLangContext(contexts);
				}
			}
			return null;
		});
	}

	protected static final List<Function<MoLangRuntimeBuilder.Input, BaseMoLangContext>> CONTEXT_SUPPLIERS = new ArrayList<>();

	protected static final List<Function<Entity, ? extends BaseMoLangContext>> ENTITY_CONTEXT_FACTORIES = new ArrayList<>();

	public static void registerEntityContext(Function<Entity, ? extends BaseMoLangContext> pFactory) {
		ENTITY_CONTEXT_FACTORIES.add(pFactory);
	}

	public static void register(@NotNull Function<MoLangRuntimeBuilder.Input, @Nullable BaseMoLangContext> pFactory) {
		CONTEXT_SUPPLIERS.add(pFactory);
	}

	public static @NotNull List<BaseMoLangContext> createContexts(MoLangRuntimeBuilder.Input pInput) {
		List<BaseMoLangContext> result = new ArrayList<>();
		for (var fn : CONTEXT_SUPPLIERS) {
			BaseMoLangContext ctx = fn.apply(pInput);
			if (ctx != null) {
				if (ctx instanceof List<?> ctxList) {
					for (Object o : ctxList) {
						if (o instanceof BaseMoLangContext c) result.add(c);
					}
				} else {
					result.add(ctx);
				}
			}
		}
		return result;
	}
}
