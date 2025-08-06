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
import software.bluelib.api.molang.context.AnimatableMoLang;
import software.bluelib.api.molang.context.BaseMoLangContext;
import software.bluelib.api.molang.context.GeneralMoLang;
import software.bluelib.api.molang.context.OperatorMoLang;
import software.bluelib.api.molang.context.math.AdvancedMathMoLang;
import software.bluelib.api.molang.context.math.BasicMathMoLang;
import software.bluelib.api.molang.context.math.RandomMoLang;
import software.bluelib.api.molang.context.math.TrigMoLang;
import software.bluelib.oldLoader.animation.AnimationState;

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
			if (ctx != null) result.add(ctx);
		}
		return result;
	}
}
