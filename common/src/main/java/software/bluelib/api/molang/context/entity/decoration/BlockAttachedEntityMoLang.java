/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.decoration;

import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class BlockAttachedEntityMoLang extends BaseMoLangContext {

	public BlockAttachedEntityMoLang(@NotNull Supplier<BlockAttachedEntity> pBlockAttachedEntitySup) {
		BlockAttachedEntity blockAttachedEntity = pBlockAttachedEntitySup.get();
		setVariable("survives", blockAttachedEntity.survives());
		setVariable("get_pos", blockAttachedEntity.getPos());

		registerFunction("drop_item", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Entity entity)) {
				return false;
			}
			blockAttachedEntity.dropItem(entity);
			return true;
		});
	}
}
