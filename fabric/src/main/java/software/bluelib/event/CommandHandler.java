package software.bluelib.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;
import software.bluelib.internal.registry.BlueCommandRegistry;

public class CommandHandler {
	public static void registerCommands(@NotNull CommandDispatcher<CommandSourceStack> pCommandSourceStackCommandDispatcher, @NotNull CommandBuildContext pCommandBuildContext, @NotNull Commands.CommandSelection pCommandSelection) {
		BlueCommandRegistry.registerCommands(pCommandSourceStackCommandDispatcher);
	}

}
