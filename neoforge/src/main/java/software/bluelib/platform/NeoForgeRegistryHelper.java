/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import software.bluelib.BlueLib;
import software.bluelib.BlueLibConstants;
import software.bluelib.net.NeoForgeNetworkManager;

import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    @Override
    public BlueLibConstants.NetworkManager getNetwork() {
        return new NeoForgeNetworkManager();
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String pId, Supplier<EntityType<T>> pEntity) {
        return BlueLib.ENTITIES.register(pId, pEntity);
    }
}
