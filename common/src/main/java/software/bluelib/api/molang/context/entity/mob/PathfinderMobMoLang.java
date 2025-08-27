/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.mob;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class PathfinderMobMoLang extends BaseMoLangContext {

	public PathfinderMobMoLang(@NotNull Supplier<PathfinderMob> pPathfinderMobSup) {
		PathfinderMob pathfinderMob = pPathfinderMobSup.get();
		setVariable("is_pathfinding", pathfinderMob.isPathFinding());
		setVariable("is_panicking", pathfinderMob.isPanicking());

		registerFunction("get_walk_target_value", (args, runtime) -> {
			if (args.size() == 1 && args.getFirst() instanceof BlockPos pos) {
				return pathfinderMob.getWalkTargetValue(pos);
			}
			if (args.size() == 2 && args.getFirst() instanceof BlockPos pos && args.get(1) instanceof LevelReader level) {
				return pathfinderMob.getWalkTargetValue(pos, level);
			}
			return 0.0;
		});
	}
}
