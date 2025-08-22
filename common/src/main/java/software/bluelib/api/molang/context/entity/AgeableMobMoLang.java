/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

import java.util.function.Supplier;

public class AgeableMobMoLang extends BaseMoLangContext {

	public AgeableMobMoLang(@NotNull Supplier<AgeableMob> pAgeableMobSup) {
		AgeableMob ageableMob = pAgeableMobSup.get();
		setVariable("get_age", ageableMob.getAge());
		setVariable("can_breed", ageableMob.canBreed());

		setVariable("baby_start_age", AgeableMob.BABY_START_AGE);

		registerFunction("get_breed_offspring", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof ServerLevel)) && (!(args.get(1) instanceof AgeableMob))) {
				return ageableMob;
			}
			ServerLevel level = (ServerLevel) args.getFirst();
			AgeableMob mate = (AgeableMob) args.get(1);
			return ageableMob.getBreedOffspring(level, mate);
		});

		registerFunction("age_up", (args, runtime) -> {
			if (args.isEmpty()) {
				return ageableMob.getAge();
			}

			Object first = args.getFirst();
			if (!(first instanceof Number)) {
				return ageableMob.getAge();
			}

			int amount = ((Number) first).intValue();

			if (args.size() == 1) {
				ageableMob.ageUp(amount);
			} else if (args.size() == 2 && args.get(1) instanceof Boolean forced) {
				ageableMob.ageUp(amount, forced);
			}

			return ageableMob.getAge();
		});

		registerFunction("set_age", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number age)) {
				return ageableMob.getAge();
			}
			int setAge = age.intValue();
			ageableMob.setAge(setAge);
			return ageableMob.getAge();
		});
	}
}
