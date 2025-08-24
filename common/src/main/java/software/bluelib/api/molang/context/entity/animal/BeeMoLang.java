/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.animal;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class BeeMoLang extends BaseMoLangContext {

	public BeeMoLang(@NotNull Supplier<Bee> pBeeSup) {
		Bee bee = pBeeSup.get();
		setVariable("flap_degrees_per_tick", Bee.FLAP_DEGREES_PER_TICK);
		setVariable("ticks_per_flap", Bee.TICKS_PER_FLAP);
		setVariable("tag_crops_grown_since_pollination", Bee.TAG_CROPS_GROWN_SINCE_POLLINATION);
		setVariable("tag_cannot_enter_hive_ticks", Bee.TAG_CANNOT_ENTER_HIVE_TICKS);
		setVariable("tag_ticks_since_pollination", Bee.TAG_TICKS_SINCE_POLLINATION);
		setVariable("tag_has_stung", Bee.TAG_HAS_STUNG);
		setVariable("tag_has_nectar", Bee.TAG_HAS_NECTAR);
		setVariable("tag_flower_pos", Bee.TAG_FLOWER_POS);
		setVariable("tag_hive_pos", Bee.TAG_HIVE_POS);

		setVariable("get_saved_flower_pos", bee.getSavedFlowerPos());
		setVariable("has_saved_flower_pos", bee.hasSavedFlowerPos());
		setVariable("get_travelling_ticks", bee.getTravellingTicks());
		setVariable("get_blacklisted_hives", bee.getBlacklistedHives());
		setVariable("has_hive", bee.hasHive());
		setVariable("get_hive_pos", bee.getHivePos());
		setVariable("get_goal_selector", bee.getGoalSelector());
		setVariable("has_nectar", bee.hasNectar());
		setVariable("has_stung", bee.hasStung());

		registerFunction("set_saved_flower_pos", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof BlockPos pos)) {
				return bee.getSavedFlowerPos();
			}
			bee.setSavedFlowerPos(pos);
			return bee.getSavedFlowerPos();
		});

		registerFunction("set_stay_out_of_hive_countdown", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number hiveCooldown)) {
				return false;
			}
			int cooldown = hiveCooldown.intValue();
			bee.setStayOutOfHiveCountdown(cooldown);
			return true;
		});

		registerFunction("get_roll_amount", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number tick)) {
				return 0.0;
			}
			float partialTick = tick.floatValue();
			return bee.getRollAmount(partialTick);
		});

		registerFunction("reset_ticks_without_nectar_since_exiting_hive", (args, runtime) -> {
			bee.resetTicksWithoutNectarSinceExitingHive();
			return true;
		});
	}
}
