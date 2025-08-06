/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AbstractHorseMoLang extends BaseMoLangContext {

	public AbstractHorseMoLang(@NotNull Supplier<AbstractHorse> pAbstractHorse) {
		setVariable("is_saddled", pAbstractHorse.get().isSaddled());
		setVariable("get_temper", pAbstractHorse.get().getTemper());
		setVariable("is_tamed", pAbstractHorse.get().isTamed());
	}
}
