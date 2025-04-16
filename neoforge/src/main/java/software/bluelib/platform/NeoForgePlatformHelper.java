// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.interfaces.platform.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String pModId) {
        return ModList.get().isLoaded(pModId);
    }

    @Override
    public Set<String> getLoadedMods() {
        return ModList.get().getMods().stream()
                .map(IModInfo::getModId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<ModMeta> getLoadedModMetadata() {
        List<ModMeta> mods = new ArrayList<>();
        for (IModInfo modInfo : ModList.get().getMods()) {
            String modId = modInfo.getModId();
            String displayName = modInfo.getDisplayName();
            String version = modInfo.getVersion().toString();
            String description = modInfo.getDescription();
            String namespace = modInfo.getNamespace();
            Map<String, Object> properties = modInfo.getModProperties();
            Optional<URL> updateURL = modInfo.getUpdateURL();
            Optional<URL> modURL = modInfo.getModURL();
            Optional<String> logoFile = modInfo.getLogoFile();
            mods.add(new ModMeta(modId, displayName, version, description, namespace, properties, updateURL, modURL, logoFile));
        }
        return mods;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
