package software.bluelib.mixin.common.molang;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.api.molang.MoLang;
import software.bluelib.api.molang.MoLangType;
import software.bluelib.api.molang.context.EntityMoLang;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstructed(CallbackInfo pCi) {
		Entity self = (Entity) (Object) this;
		MoLang.service.getRuntimeFor(MoLangType.ENTITY).registerContext(MoLangType.ENTITY.id(), new EntityMoLang(self));
	}
}
