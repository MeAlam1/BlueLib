package software.bluelib.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import software.bluelib.commands.OpenLoggerScreenCommand;

public class CommandRegistry {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> pDispatcher) {
        OpenLoggerScreenCommand.register(pDispatcher);
    }
}
