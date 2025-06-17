package software.bluelib.api.molang.context;

import net.minecraft.world.entity.LivingEntity;

public class LivingEntityMoLang extends BaseMoLangContext {

	public LivingEntityMoLang(LivingEntity pEntity) {
		setVariable("health", pEntity.getHealth());
	}
}
