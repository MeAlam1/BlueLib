/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import net.minecraft.world.entity.Entity;
import software.bluelib.api.molang.expression.MoLangExpression;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimationState;

public class MoLangUtils {

	public static Object state(String pExpression, AnimationState<? extends BlueAnimatable> pState) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_state", (java.util.function.Supplier<?>) () -> pState));
	}

	public static Object state(MoLangExpression pExpression, AnimationState<? extends BlueAnimatable> pState) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_state", (java.util.function.Supplier<?>) () -> pState));
	}

	public static Object entity(String pExpression, Entity pEntity) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_entity", (java.util.function.Supplier<?>) () -> pEntity));
	}

	public static Object entity(MoLangExpression pExpression, Entity pEntity) {
		return MoLang.evaluate(pExpression, builder -> builder.with("bluelib_entity", (java.util.function.Supplier<?>) () -> pEntity));
	}
}
