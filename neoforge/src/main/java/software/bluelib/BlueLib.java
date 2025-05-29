/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.api.registry.BlueRegistryBuilder;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.builders.menu.MenuBuilder;
import software.bluelib.api.registry.helpers.menu.BlueMenuTypeExtension;
import software.bluelib.client.BlueLibClient;
import software.bluelib.config.ConfigHolder;
import software.bluelib.event.ReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.NeoForgeNetworkManager;
import software.bluelib.platform.NeoForgeRegistryHelper;

import java.util.function.Supplier;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

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

    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        BlueLibCommon.doRegistration();
        ReloadHandler.registerProvider(new VariantProvider());
        NeoForgeRegistryHelper.register(pModEventBus);
        pModEventBus.register(this);
        MixinBootstrap.init();
        pModEventBus.addListener(NeoForgeNetworkManager::registerMessages);
        pModEventBus.addListener(GatherDataEvent.class, this::onGatherData);

        if (FMLEnvironment.dist == Dist.CLIENT)
            BlueLibClient.init(pModContainer);

        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, BlueLibConstants.MOD_ID + "-markdown.toml");
        pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.LOGGER_SPEC, BlueLibConstants.MOD_ID + "-logger.toml");
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        BlueLibCommon.init();
    }

    private void onGatherData(GatherDataEvent event) {
        AbstractRegistryBuilder.doDatagen();
    }
}
