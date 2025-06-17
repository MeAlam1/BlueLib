package software.bluelib.api.molang.context;

import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.loading.math.MathParser;
import software.bluelib.loader.loading.math.MoLangQueries;

public class AnimatableMoLang extends BaseMoLangContext {

	public AnimatableMoLang(AnimationState<? extends BlueAnimatable> pState) {
		var controller = pState.getController();
		setVariable("anim_time", controller != null ? controller.getAnimTime() : 0d);
		MathParser.setVariable(MoLangQueries.ANIM_TIME, () -> controller != null ? controller.getAnimTime() : 0d);
	}
}
