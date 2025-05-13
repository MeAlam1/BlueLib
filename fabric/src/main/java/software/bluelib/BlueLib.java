// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import software.bluelib.api.registry.helpers.entity.AttributeHelper;
import software.bluelib.api.registry.helpers.entity.RenderHelper;
import software.bluelib.config.ConfigLoader;
import software.bluelib.event.ChatHandler;
import software.bluelib.example.event.ReloadHandler;
import software.bluelib.net.FabricNetworkManager;

public class BlueLib implements ModInitializer, DataGeneratorEntrypoint {

    private boolean hasInitialized = false;

    @Override
    public void onInitialize() {
        BlueLibCommon.doRegistration();
        FabricNetworkManager.registerMessages();
        FabricNetworkManager.registerServerHandlers();
        AttributeHelper.registerAttributes(FabricDefaultAttributeRegistry::register);
        RenderHelper.registerRenderers(EntityRendererRegistry::register, BlockEntityRenderers::register);
        registerModEventListeners();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!hasInitialized) {
                    hasInitialized = true;
                    BlueLibCommon.init();
                }
            });
        }
    }

    public static void registerModEventListeners() {
        ServerLifecycleEvents.SERVER_STARTING.register(ReloadHandler::onServerStart);
        ServerLifecycleEvents.SERVER_STARTED.register(ConfigLoader::createConfigs);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ConfigLoader::reloadConfigs);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ReloadHandler::onReload);
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatHandler::onAllowChat);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        BlueLibCommon.doDatagen(BlueLibConstants.MOD_ID);
    }
}
