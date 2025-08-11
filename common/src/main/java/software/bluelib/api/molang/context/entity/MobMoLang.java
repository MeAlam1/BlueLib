/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class MobMoLang extends BaseMoLangContext {

	public MobMoLang(@NotNull Supplier<Mob> pMob) {
		setVariable("get_target", pMob.get().getTarget());
		setVariable("get_brain", pMob.get().getBrain());
		setVariable("get_navigation", pMob.get().getNavigation());
		setVariable("get_look_control", pMob.get().getLookControl());
		setVariable("get_move_control", pMob.get().getMoveControl());
		setVariable("get_jump_control", pMob.get().getJumpControl());
		setVariable("can_attack", pMob.get().getTarget() == null ? null : pMob.get().canAttack(pMob.get().getTarget())); // TODO: NULL CHECK
		setVariable("can_pickup_loot", pMob.get().canPickUpLoot());
		setVariable("get_leash_holder", pMob.get().getLeashHolder());
		setVariable("get_no_action_time", pMob.get().getNoActionTime());
	}
}
