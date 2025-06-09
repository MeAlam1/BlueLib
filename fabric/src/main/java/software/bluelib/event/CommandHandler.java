package software.bluelib.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import software.bluelib.internal.registry.BlueCommandRegistry;

public class CommandHandler {
	public static void registerCommands(CommandDispatcher<CommandSourceStack> pCommandSourceStackCommandDispatcher, CommandBuildContext pCommandBuildContext, Commands.CommandSelection pCommandSelection) {
		BlueCommandRegistry.registerCommands(pCommandSourceStackCommandDispatcher);
	}

}
