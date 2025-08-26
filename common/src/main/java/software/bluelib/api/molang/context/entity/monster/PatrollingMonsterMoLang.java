/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.monster;

import java.util.function.Supplier;
import net.minecraft.world.entity.monster.PatrollingMonster;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class PatrollingMonsterMoLang extends BaseMoLangContext {

	public PatrollingMonsterMoLang(@NotNull Supplier<PatrollingMonster> pPatrollingMonsterSup) {
		PatrollingMonster pPatrollingMonster = pPatrollingMonsterSup.get();
		setVariable("can_be_leader", pPatrollingMonster.canBeLeader());
		setVariable("get_patrol_target", pPatrollingMonster.getPatrolTarget());
		setVariable("has_patrol_target", pPatrollingMonster.hasPatrolTarget());
		setVariable("is_patrol_leader", pPatrollingMonster.isPatrolLeader());
		setVariable("can_join_patrol", pPatrollingMonster.canJoinPatrol());

		registerFunction("set_patrol_leader", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean leader)) {
				return pPatrollingMonster.isPatrolLeader();
			}
			pPatrollingMonster.setPatrolLeader(leader);
			return pPatrollingMonster.isPatrolLeader();
		});

		registerFunction("find_patrol_target", (args, runtime) -> {
			pPatrollingMonster.findPatrolTarget();
			return pPatrollingMonster.getPatrolTarget();
		});
	}
}
