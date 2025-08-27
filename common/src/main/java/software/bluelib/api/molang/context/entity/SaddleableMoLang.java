/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class SaddleableMoLang extends BaseMoLangContext {

	public SaddleableMoLang(@NotNull Supplier<Saddleable> pSaddleableSup) {
		Saddleable saddleable = pSaddleableSup.get();
		setVariable("is_saddleable", saddleable.isSaddleable());
		setVariable("is_saddled", saddleable.isSaddled());
		setVariable("get_saddle_sound_event", saddleable.getSaddleSoundEvent());

		registerFunction("equip_saddle", (args, runtime) -> {
			if (!(args.getFirst() instanceof ItemStack item)) {
				return saddleable.isSaddled();
			}
			SoundSource soundSource = null;
			if (args.size() > 1 && args.get(1) instanceof SoundSource) {
				soundSource = (SoundSource) args.get(1);
			}
			saddleable.equipSaddle(item, soundSource);
			return saddleable.isSaddled();
		});
	}
}
