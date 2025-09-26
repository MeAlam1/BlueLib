/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangExpression;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationState;

public class MoLangUtils {

	@Nullable
	public static Object state(@NotNull String pExpression, @NotNull AnimationState<? extends BlueAnimatable> pState) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_state", (java.util.function.Supplier<?>) () -> pState));
	}

	@Nullable
	public static Object state(@NotNull MoLangExpression pExpression, @NotNull AnimationState<? extends BlueAnimatable> pState) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_state", (java.util.function.Supplier<?>) () -> pState));
	}

	@Nullable
	public static Object entity(@NotNull String pExpression, @NotNull Entity pEntity) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_entity", (java.util.function.Supplier<?>) () -> pEntity));
	}

	@Nullable
	public static Object entity(@NotNull MoLangExpression pExpression, @NotNull Entity pEntity) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_entity", (java.util.function.Supplier<?>) () -> pEntity));
	}
}
