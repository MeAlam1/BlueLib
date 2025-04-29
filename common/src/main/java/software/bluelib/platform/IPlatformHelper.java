// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import java.util.List;
import java.util.Set;
import software.bluelib.api.event.mod.ModMeta;

@SuppressWarnings("unused")
public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String pModId);

    Set<String> getLoadedMods();

    List<ModMeta> getLoadedModMetadata();

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
