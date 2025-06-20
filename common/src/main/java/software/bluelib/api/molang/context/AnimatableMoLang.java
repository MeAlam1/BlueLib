/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.loading.math.MathParser;
import software.bluelib.loader.loading.math.MoLangQueries;

public class AnimatableMoLang extends BaseMoLangContext {

    public AnimatableMoLang(AnimationState<? extends BlueAnimatable> pState) {
        setVariable("anim_time", pState.getController() != null ? pState.getController().getAnimTime() : 0d);
        MathParser.setVariable(MoLangQueries.ANIM_TIME, () -> pState.getController() != null ? pState.getController().getAnimTime() : 0d);
    }
}
