/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.animal;

import java.util.function.Supplier;
import net.minecraft.world.entity.animal.FlyingAnimal;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class FlyingAnimalMoLang extends BaseMoLangContext {

	public FlyingAnimalMoLang(@NotNull Supplier<FlyingAnimal> pFlyingAnimalSup) {
		FlyingAnimal flyingAnimal = pFlyingAnimalSup.get();
		setVariable("is_flying", flyingAnimal.isFlying());
	}
}
