package software.bluelib.api.event.mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class ModIntegration {

    private static final List<ModMeta> loadedModMetas = new ArrayList<>();

    public static void checkSupportMods() {
        Set<String> excludedMods = Set.of(
                "minecraft",
                "neoforge",
                "fabric",
                "fabric-renderer-api-v1",
                "fabric-keybindings-v0",
                "fabricloader",
                "fabric-transfer-api-v1",
                "fabric-dimensions-v1",
                "fabric-object-builder-api-v1",
                "java",
                "fabric-game-rule-api-v1",
                "fabric-resource-conditions-api-v1",
                "fabric-api-base",
                "fabric-sound-api-v1",
                "fabric-model-loading-api-v1",
                "fabric-rendering-data-attachment-v1",
                "mixinextras",
                "fabric-data-attachment-api-v1",
                "fabric-rendering-fluids-v1",
                "fabric-blockrenderlayer-v1",
                "fabric-lifecycle-events-v1",
                "fabric-renderer-registries-v1",
                "fabric-particles-v1",
                "fabric-data-generation-api-v1",
                "fabric-api-lookup-api-v1",
                "fabric-screen-handler-api-v1",
                "fabric-command-api-v2",
                "fabric-block-api-v1",
                "fabric-command-api-v1",
                "fabric-screen-api-v1",
                "fabric-renderer-indigo",
                "fabric-convention-tags-v2",
                "fabric-api",
                "fabric-events-interaction-v0",
                "fabric-item-api-v1",
                "fabric-crash-report-info-v1",
                "fabric-convention-tags-v1",
                "fabric-item-group-api-v1",
                "fabric-recipe-api-v1",
                "fabric-entity-events-v1",
                "fabric-rendering-v0",
                "fabric-key-binding-api-v1",
                "fabric-rendering-v1",
                "fabric-resource-loader-v0",
                "fabric-content-registries-v0",
                "fabric-block-view-api-v2",
                "fabric-biome-api-v1",
                "fabric-gametest-api-v1",
                "fabric-client-tags-api-v1",
                "fabric-loot-api-v3",
                "fabric-loot-api-v2",
                "fabric-message-api-v1",
                "fabric-commands-v0",
                "fabric-registry-sync-v0",
                "fabric-transitive-access-wideners-v1",
                "fabric-networking-api-v1");

        Set<String> loadedMods = BlueLibCommon.PLATFORM.getLoadedMods().stream()
                .filter(mod -> !excludedMods.contains(mod))
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
