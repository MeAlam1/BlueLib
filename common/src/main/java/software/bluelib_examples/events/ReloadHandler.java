// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib_examples.events;

import net.minecraft.server.MinecraftServer;
import software.bluelib.event.ReloadEventHandler;
import software.bluelib_examples.BlueLibConstants;

public class ReloadHandler extends ReloadEventHandler {

    private static final String BASE_PATH = "variant/entity/";

    public static void loadEntityVariants(MinecraftServer pServer) {
        String folderPath = BASE_PATH + "example";
        ReloadEventHandler.registerEntityVariants(folderPath, pServer, BlueLibConstants.MOD_ID, "example");
    }
}
