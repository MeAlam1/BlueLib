/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.event.mod.ModMeta;

@SuppressWarnings("unused")
public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String pModId);

    Set<String> getLoadedMods();

    List<ModMeta> getLoadedModMetadata();

    boolean isDevelopmentEnvironment();

    boolean isPhysicalClient();

    Path getGameDir();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    BlueLibConstants.Environment getEnvironment();

    BlueLibConstants.ModAPI getAPI();

    MinecraftServer getServer();
}
