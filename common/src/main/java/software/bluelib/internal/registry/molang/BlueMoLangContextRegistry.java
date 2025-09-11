/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry.molang;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
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

@ApiStatus.Internal
public class BlueMoLangContextRegistry {

	public static void init() {
		BlueMoLangContextRegistry.register(input -> new GeneralMoLang());
		BlueMoLangContextRegistry.register(input -> new BasicMathMoLang());
		BlueMoLangContextRegistry.register(input -> new AdvancedMathMoLang());
		BlueMoLangContextRegistry.register(input -> new RandomMoLang());
		BlueMoLangContextRegistry.register(input -> new TrigMoLang());
		BlueMoLangContextRegistry.register(input -> new OperatorMoLang());

		BlueMoLangContextRegistry.register(input -> {
			Supplier<?> supplier = input.get("bluelib_state", Supplier.class);
			if (supplier != null) {
				Object value = supplier.get();
				if (value instanceof AnimationState<?> animationState) {
					return new AnimatableMoLang(animationState);
				}
			}
			return null;
		});

		BlueMoLangEntityRegistry.init();

		BlueMoLangContextRegistry.register(input -> {
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

	@NotNull
	protected static final List<Function<MoLangRuntimeBuilder.Input, BaseMoLangContext>> CONTEXT_SUPPLIERS = new ArrayList<>();

	@NotNull
	protected static final List<Function<Entity, ? extends BaseMoLangContext>> ENTITY_CONTEXT_FACTORIES = new ArrayList<>();

	public static void registerEntityContext(@NotNull Function<Entity, ? extends BaseMoLangContext> pFactory) {
		ENTITY_CONTEXT_FACTORIES.add(pFactory);
	}

	public static void register(@NotNull Function<MoLangRuntimeBuilder.Input, @Nullable BaseMoLangContext> pFactory) {
		CONTEXT_SUPPLIERS.add(pFactory);
	}

	public static @NotNull List<BaseMoLangContext> createContexts(@NotNull MoLangRuntimeBuilder.Input pInput) {
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
