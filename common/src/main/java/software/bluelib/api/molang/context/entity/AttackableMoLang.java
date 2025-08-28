/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.Attackable;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AttackableMoLang extends BaseMoLangContext {

	public AttackableMoLang(@NotNull Supplier<Attackable> pAttackableSup) {
		Attackable attackableEntity = pAttackableSup.get();
		setVariable("get_last_attacker", attackableEntity.getLastAttacker());
	}
}
