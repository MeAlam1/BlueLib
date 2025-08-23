/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.npc;

import java.util.function.Supplier;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AbstractVillagerMoLang extends BaseMoLangContext {

	public AbstractVillagerMoLang(@NotNull Supplier<AbstractVillager> pAbstractVillagerSup) {
		AbstractVillager abstractVillager = pAbstractVillagerSup.get();

		setVariable("villager_slot_offset", AbstractVillager.VILLAGER_SLOT_OFFSET);
		setVariable("get_unhappy_counter", abstractVillager.getUnhappyCounter());
		setVariable("is_trading", abstractVillager.isTrading());

		registerFunction("set_unhappy_counter", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number)) {
				return abstractVillager.getUnhappyCounter();
			}
			int unhappy = ((Number) args.getFirst()).intValue();
			abstractVillager.setUnhappyCounter(unhappy);
			return abstractVillager.getUnhappyCounter();
		});

		registerFunction("play_celebrate_sound", (args, runtime) -> {
			abstractVillager.playCelebrateSound();
			return true;
		});
	}
}
