/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.client.BlueLibClient;
import software.bluelib.config.ConfigHolder;
import software.bluelib.event.ReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.NeoForgeNetworkManager;
import software.bluelib.registry.BlueEntityRegistry;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS_REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BlueLibConstants.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, BlueLibConstants.MOD_ID);

	public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
		DATA_COMPONENTS_REGISTER.register(pModEventBus);
		BlueLibConstants.init();
		ENTITIES.register(pModEventBus);
		pModEventBus.<EntityAttributeCreationEvent>addListener(event -> BlueEntityRegistry.registerEntityAttributes(event::put));

		BlueLibCommon.doServerRegistration();
		ReloadHandler.registerProvider(new VariantProvider());
		pModEventBus.register(this);
		MixinBootstrap.init();
		pModEventBus.addListener(NeoForgeNetworkManager::registerMessages);

		if (FMLEnvironment.dist == Dist.CLIENT)
			BlueLibClient.init(pModContainer);

		pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.MARKDOWN_SPEC, BlueLibConstants.MOD_ID + "-markdown.toml");
		pModContainer.registerConfig(ModConfig.Type.SERVER, ConfigHolder.LOGGER_SPEC, BlueLibConstants.MOD_ID + "-logger.toml");
	}

	@SubscribeEvent
	public void onLoadComplete(FMLClientSetupEvent pEvent) {
		BlueLibCommon.init();
	}
}
