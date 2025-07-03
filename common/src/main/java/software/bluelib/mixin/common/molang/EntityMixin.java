/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.common.molang;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.api.molang.MoLang;
import software.bluelib.api.molang.MoLangType;
import software.bluelib.api.molang.context.EntityMoLang;

// TODO: Inefficient, but works for now
@Mixin(Entity.class)
public class EntityMixin {

	@Unique
	private boolean bluelib$MoLangRegistered = false;

	@Inject(method = "tick", at = @At("HEAD"))
	private void onConstructed(@NotNull CallbackInfo pCi) {
		if (!bluelib$MoLangRegistered) {
			bluelib$MoLangRegistered = true;
			Entity self = (Entity) (Object) this;

			MoLang.service.getRuntimeFor(MoLangType.ENTITY).registerContext(MoLangType.ENTITY.id(), new EntityMoLang(() -> self));
		}
	}
}
