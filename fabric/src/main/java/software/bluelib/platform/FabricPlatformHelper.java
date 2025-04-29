// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import java.util.*;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import software.bluelib.api.event.mod.ModMeta;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String pModId) {
        return FabricLoader.getInstance().isModLoaded(pModId);
    }

    @Override
    public Set<String> getLoadedMods() {
        return FabricLoader.getInstance().getAllMods().stream()
                .map(modContainer -> modContainer.getMetadata().getId())
                .collect(Collectors.toSet());
    }

    @Override
    public List<ModMeta> getLoadedModMetadata() {
        List<ModMeta> mods = new ArrayList<>();
        for (ModContainer modInfo : FabricLoader.getInstance().getAllMods()) {
            String modId = modInfo.getMetadata().getId();
            String displayName = modInfo.getMetadata().getName();
            String version = modInfo.getMetadata().getVersion().toString();
            String description = modInfo.getMetadata().getDescription();
            Optional<String> logoFile = modInfo.getMetadata().getIconPath(128);
            mods.add(new ModMeta(modId, displayName, version, description, logoFile));
        }
        return mods;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
