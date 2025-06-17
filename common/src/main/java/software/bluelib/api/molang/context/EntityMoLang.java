package software.bluelib.api.molang.context;

import net.minecraft.world.entity.LivingEntity;

public class EntityMoLang extends BaseMoLangContext {

    public EntityMoLang(LivingEntity pEntity) {
        variables.put("health", pEntity.getHealth());
    }
}
