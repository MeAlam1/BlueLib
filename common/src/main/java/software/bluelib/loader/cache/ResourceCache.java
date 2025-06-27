package software.bluelib.loader.cache;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.BlueLoader;
import software.bluelib.loader.json.deserialize.variants.Entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceCache extends BlueLoader {
	public static Map<ResourceLocation, Entity> VARIANTS = Collections.emptyMap();

	public static Map<ResourceLocation, Entity> getVariants() {
		return VARIANTS;
	}

	public static void registerServerReloadListener(MinecraftServer pServer, List<IVariantProvider> pProviders) {
		System.out.println("Registering server reload listener");
		ResourceCache.reloadServer(pProviders, pServer.getResourceManager(), Util.backgroundExecutor(), pServer);
	}

	public static CompletableFuture<Void> reloadServer(
			List<IVariantProvider> pProviders,
			ResourceManager pResourceManager,
			Executor pBackgroundExecutor,
			Executor pGameExecutor) {
		clearServerCaches();
		
		System.out.println("Loading Variants");
		CompletableFuture<Map<ResourceLocation, Entity>> variants = loadVariants(pBackgroundExecutor, pResourceManager, pProviders);

		return CompletableFuture.allOf(variants)
				.thenRunAsync(() -> {
					ResourceCache.VARIANTS = variants.join();

					BaseLogger.log(true, BaseLogLevel.INFO, "Variants Cache: " + ResourceCache.VARIANTS);
				}, pGameExecutor);
	}


	public static void clearServerCaches() {
		ResourceCache.VARIANTS = Collections.emptyMap();
	}
}
