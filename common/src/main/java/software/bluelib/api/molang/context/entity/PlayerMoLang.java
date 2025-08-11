/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class PlayerMoLang extends BaseMoLangContext {

	public PlayerMoLang(@NotNull Supplier<Player> pPlayer) {
		setVariable("get_xp", pPlayer.get().totalExperience);
		setVariable("get_level", pPlayer.get().experienceLevel);
		setVariable("get_hunger", pPlayer.get().getFoodData().getFoodLevel());
		setVariable("get_saturation", pPlayer.get().getFoodData().getSaturationLevel());
		setVariable("is_crouching", pPlayer.get().isCrouching());
		setVariable("is_swimming", pPlayer.get().isSwimming());
		setVariable("get_sleep_timer", pPlayer.get().getSleepTimer());
	}
}
