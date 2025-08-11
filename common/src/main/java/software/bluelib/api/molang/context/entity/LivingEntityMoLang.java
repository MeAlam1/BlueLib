/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class LivingEntityMoLang extends BaseMoLangContext {

	public LivingEntityMoLang(@NotNull Supplier<LivingEntity> pLivingEntity) {
		setVariable("get_health", pLivingEntity.get().getHealth());
		setVariable("get_max_health", pLivingEntity.get().getMaxHealth());
		setVariable("get_main_hand_item", pLivingEntity.get().getMainHandItem());
		setVariable("get_offhand_item", pLivingEntity.get().getOffhandItem());
		setVariable("get_armor_items", pLivingEntity.get().getArmorSlots());

		setVariable("is_baby", pLivingEntity.get().isBaby());
		setVariable("is_sleeping", pLivingEntity.get().isSleeping());
		setVariable("is_fall_flying", pLivingEntity.get().isFallFlying());

		setVariable("get_last_hurt_by_entity", pLivingEntity.get().getLastHurtByMob());
		setVariable("get_last_damage_source", pLivingEntity.get().getLastDamageSource());
		setVariable("get_active_effects", pLivingEntity.get().getActiveEffects());
		setVariable("get_use_item", pLivingEntity.get().getUseItem());

		setVariable("is_using_item", pLivingEntity.get().isUsingItem());
		setVariable("is_glowing", pLivingEntity.get().isCurrentlyGlowing());
		setVariable("is_dead_or_dying", pLivingEntity.get().isDeadOrDying());

		setVariable("get_absorption_amount", pLivingEntity.get().getAbsorptionAmount());
		setVariable("get_arrow_count", pLivingEntity.get().getArrowCount());
	}
}
