/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

import org.jetbrains.annotations.NotNull;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.loading.math.MathParser;
import software.bluelib.oldLoader.loading.math.MoLangQueries;

public class AnimatableMoLang extends BaseMoLangContext {

	public AnimatableMoLang(@NotNull AnimationState<? extends BlueAnimatable> pState) {
		setVariable("anim_time", pState.getController() != null ? pState.getController().getAnimTime() : 0d);
		MathParser.setVariable(MoLangQueries.ANIM_TIME, () -> pState.getController() != null ? pState.getController().getAnimTime() : 0d);
	}
}
