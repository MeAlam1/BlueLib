/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import software.bluelib.commands.OpenLoggerScreenCommand;

public class CommandRegistry {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> pDispatcher) {
        OpenLoggerScreenCommand.register(pDispatcher);
    }
}
