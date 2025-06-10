/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.net.messages.client.OpenLoggerPacket;

public class OpenLoggerScreenCommand {

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register(
                Commands.literal("log")
                        .executes(OpenLoggerScreenCommand::openLogScreen));
    }

    private static int openLogScreen(CommandContext<CommandSourceStack> pContext) {
        ServerPlayer player = pContext.getSource().getPlayer();
        assert player != null;
        if (player.hasPermissions(3)) {
            NetworkRegistry.sendPacketToPlayer(player, new OpenLoggerPacket());
        } else {
            pContext.getSource().sendFailure(BlueTranslation.translate("command.logger.no_permission"));
        }
        return 1;
    }
}
