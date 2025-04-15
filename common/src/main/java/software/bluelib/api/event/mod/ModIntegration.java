package software.bluelib.api.event.mod;

import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModIntegration {
    private static final List<ModMeta> loadedModMetas = new ArrayList<>();

    public static void checkSupportMods() {
        Set<String> loadedMods = BlueLibCommon.PLATFORM.getLoadedMods().stream()
                .filter(mod -> !mod.equals("minecraft") && !mod.equals("neoforge") && !mod.equals("fabric"))
                .collect(Collectors.toSet());

        if (!loadedMods.isEmpty()) {
            StringBuilder modsMessage = new StringBuilder("Mods Loaded:\n");
            for (String mod : loadedMods) {
                ModMeta modMeta = getModMeta(mod);
                if (modMeta != null) {
                    loadedModMetas.add(modMeta);
                    BlueLibCommon.EVENT_PROXY.onModLoaded(modMeta);
                }
                modsMessage.append(mod).append("\n");
            }
            BlueLibCommon.EVENT_PROXY.onAllModsLoaded(loadedModMetas);
            BaseLogger.log(BaseLogLevel.INFO, modsMessage.toString());
        } else {
            BlueLibCommon.EVENT_PROXY.onAllModsLoaded(loadedModMetas);
            BaseLogger.log(BaseLogLevel.INFO, "No supported mods loaded.");
        }
    }

    public static ModMeta getModMeta(String pModId) {
        return BlueLibCommon.PLATFORM.getLoadedModMetadata().stream()
                .filter(modMeta -> modMeta.modId().equals(pModId))
                .findFirst()
                .orElse(null);
    }

    public static List<ModMeta> getLoadedModMetas() {
        return loadedModMetas;
    }
}