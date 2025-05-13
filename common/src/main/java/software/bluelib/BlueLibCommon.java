// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import static software.bluelib.BlueLibConstants.SCHEDULER;

import java.util.concurrent.TimeUnit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.event.mod.ModIntegration;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.registry.builders.blocks.BlockBuilder;
import software.bluelib.api.registry.builders.entity.EntityBuilder;
import software.bluelib.api.registry.builders.items.ItemBuilder;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class BlueLibCommon {

    private BlueLibCommon() {}

    public static void init() {
        if (isDeveloperMode()) {
            SCHEDULER.schedule(() -> {
                ModIntegration.checkSupportMods();
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(BlueLibCommon.Translation.translate("mod.thank_you"));
                BaseLogger.logBlueLib(BlueLibCommon.Translation.translate("mod.thank_you.subtitle"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                SCHEDULER.shutdown();
            }, 5, TimeUnit.SECONDS);
        }
    }

    public static void doRegistration() {
        var networkRegistry = new software.bluelib.registry.NetworkRegistry();
        NetworkRegistry.registerC2SPacketProvider(networkRegistry);
        NetworkRegistry.registerS2CPacketProvider(networkRegistry);
    }

    public static void doDatagen(String modId) {
        ItemBuilder.doItemModelGen(modId);
        BlockBuilder.doBlockModelGen(modId);
        EntityBuilder.doSpawnEggDatagen(modId);
    }

    public static boolean isDeveloperMode() {
        boolean isDevMode = BlueLibConstants.PlatformHelper.PLATFORM.isDevelopmentEnvironment();
        if (isDevMode) {
            BaseLogger.log(BaseLogLevel.INFO, Component.literal("Running in Developer mode."), true);
        }
        return isDevMode;
    }

    @ApiStatus.Internal
    public static class Resource {

        public static ResourceLocation resource(String pPath) {
            return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pPath);
        }
    }

    @ApiStatus.Internal
    public static class Translation {

        public static Component translate(String pString) {
            return Component.translatable(BlueLibConstants.MOD_ID + "." + pString);
        }

        public static Component translate(String pString, Object... pArgs) {
            return Component.translatable(BlueLibConstants.MOD_ID + "." + pString, pArgs);
        }

        public static Component log(String pString) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString);
        }

        public static Component log(String pString, Object... pArgs) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString, pArgs);
        }

        public static Component config(String pString) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString);
        }

        public static Component config(String pString, Object... pArgs) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString, pArgs);
        }
    }
}
