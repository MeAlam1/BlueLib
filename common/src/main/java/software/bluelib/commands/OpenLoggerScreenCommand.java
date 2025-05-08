// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.api.net.NetworkRegistry;

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
            pContext.getSource().sendFailure(Component.translatable("bluelib.command.logger.no_permission"));
        }
        return 1;
    }
}
