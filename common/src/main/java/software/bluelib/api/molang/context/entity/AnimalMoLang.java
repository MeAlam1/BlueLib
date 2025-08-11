/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AnimalMoLang extends BaseMoLangContext {

	public AnimalMoLang(@NotNull Supplier<Animal> pAnimal) {
		setVariable("is_in_love", pAnimal.get().isInLove());
	}
}
