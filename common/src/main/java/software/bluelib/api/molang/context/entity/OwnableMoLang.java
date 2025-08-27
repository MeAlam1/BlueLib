/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.OwnableEntity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class OwnableMoLang extends BaseMoLangContext {

	public OwnableMoLang(@NotNull Supplier<OwnableEntity> pOwnableEntitySup) {
		OwnableEntity ownableEntity = pOwnableEntitySup.get();
		setVariable("get_owner", ownableEntity.getOwner());
		setVariable("level", ownableEntity.level());
		setVariable("get_owner_uuid", ownableEntity.getOwnerUUID());
	}
}
