/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.commands.OpenLoggerScreenCommand;

@ApiStatus.Internal
public class BlueCommandRegistry {

    public static void registerCommands(@NotNull CommandDispatcher<CommandSourceStack> pDispatcher) {
        OpenLoggerScreenCommand.register(pDispatcher);
    }
}
