/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;
import software.bluelib.internal.registry.BlueCommandRegistry;

@SuppressWarnings({ "unused" })
public class CommandHandler {

	public static void registerCommands(@NotNull CommandDispatcher<CommandSourceStack> pCommandSourceStackCommandDispatcher, @NotNull CommandBuildContext pCommandBuildContext, @NotNull Commands.CommandSelection pCommandSelection) {
		BlueCommandRegistry.registerCommands(pCommandSourceStackCommandDispatcher);
	}
}
