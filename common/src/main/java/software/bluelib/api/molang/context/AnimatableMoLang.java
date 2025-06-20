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
