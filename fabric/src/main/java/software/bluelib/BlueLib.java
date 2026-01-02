package software.bluelib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import software.bluelib.event.FabricReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.internal.registry.BlueEntityRegistry;
import software.bluelib.net.BlueLibNetworkDiagnostics;
import software.bluelib.net.FabricHandlerRegistrar;
import software.bluelib.net.FabricNetworkManager;

public class BlueLib implements ModInitializer {

	private boolean hasInitialized = false;

	@Override
	public void onInitialize() {
		FabricEvents.register();

		BlueLibCommon.doRegistration();

		FabricNetworkManager.registerServerPackets();
		FabricHandlerRegistrar.registerServerHandlers();

		BlueLibNetworkDiagnostics.registerServer();

		BlueEntityRegistry.registerEntityAttributes(FabricDefaultAttributeRegistry::register);

		FabricReloadHandler.registerProvider(new VariantProvider());
		clientEndTick();
	}

	@NotNull
	private Boolean isClientEnvironment() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	private void clientEndTick() {
		if (isClientEnvironment()) {
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				if (!hasInitialized) {
					hasInitialized = true;
					BlueLibCommon.init();
				}
			});
		}
	}
}