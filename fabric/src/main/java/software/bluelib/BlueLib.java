/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import software.bluelib.api.registry.BlueRegistryBuilder;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.helpers.entity.AttributeHelper;
import software.bluelib.api.registry.helpers.entity.RenderHelper;
import software.bluelib.config.ConfigLoader;
import software.bluelib.event.ChatHandler;
import software.bluelib.event.CommandHandler;
import software.bluelib.event.ReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.FabricNetworkManager;

public class BlueLib implements ModInitializer, DataGeneratorEntrypoint {

    /**
     * Initializes the {@link AbstractRegistryBuilder} instance with the mod ID. Replace {@link BlueLibConstants#MOD_ID} with your mod's unique mod ID to register content under your mod's namespace.
     * <p>
     * This is essential for registering mod content such as items, blocks, and entities.
     * <p>
     * <strong>Do not remove</strong>, as it will break the mod's registration system.
     * <p>
     * Do not use this, you need to add this line into your own mod.
     */
    public static AbstractRegistryBuilder REGISTRY = new BlueRegistryBuilder(BlueLibConstants.MOD_ID);

    private boolean hasInitialized = false;

    @Override
    public void onInitialize() {
        ReloadHandler.registerProvider(new VariantProvider());
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
        CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        AbstractRegistryBuilder.doDatagen();
    }
}
