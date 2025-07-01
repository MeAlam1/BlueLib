/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;

@ApiStatus.Internal
public class BlueResource {

    @NotNull
    public static ResourceLocation resource(@NotNull String pPath) {
        return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pPath);
    }
}
