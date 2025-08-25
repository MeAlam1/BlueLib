/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.monster;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class MonsterMoLang extends BaseMoLangContext {

	public MonsterMoLang(@NotNull Supplier<Monster> pMonsterSup) {
		Monster monster = pMonsterSup.get();

		registerFunction("is_dark_enough_to_spawn", (args, runtime) -> {
			if (args.size() != 3
					|| (!(args.getFirst() instanceof ServerLevelAccessor)
							&& !(args.get(1) instanceof BlockPos)
							&& !(args.get(2) instanceof RandomSource))) {
				return false;
			}
			ServerLevelAccessor level = (ServerLevelAccessor) args.getFirst();
			BlockPos pos = (BlockPos) args.get(1);
			RandomSource random = (RandomSource) args.get(2);
			return Monster.isDarkEnoughToSpawn(level, pos, random);
		});

		registerFunction("is_preventing_player_rest", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Player player)) {
				return false;
			}
			return monster.isPreventingPlayerRest(player);
		});
	}
}
