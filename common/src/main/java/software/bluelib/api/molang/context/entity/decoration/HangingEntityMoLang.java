/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.decoration;

import java.util.function.Supplier;
import net.minecraft.world.entity.decoration.HangingEntity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class HangingEntityMoLang extends BaseMoLangContext {

	public HangingEntityMoLang(@NotNull Supplier<HangingEntity> pHangingEntitySup) {
		HangingEntity hangingEntity = pHangingEntitySup.get();
		registerFunction("play_placement_sound", (args, runtime) -> {
			hangingEntity.playPlacementSound();
			return true;
		});
	}
}
