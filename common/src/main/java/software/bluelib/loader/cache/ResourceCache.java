package software.bluelib.loader.cache;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.loader.BlueLoader;
import software.bluelib.loader.cache.variants.EntityCache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceCache extends BlueLoader {
	public static Map<ResourceLocation, EntityCache> VARIANTS = Collections.emptyMap();

	public static Map<ResourceLocation, EntityCache> getVariants() {
		return VARIANTS;
	}

	public static void registerServerReloadListener(MinecraftServer pServer, List<IVariantProvider> pProviders) {
		ResourceCache.reloadServer(pProviders, pServer.getResourceManager(), Util.backgroundExecutor(), pServer);
	}

	public static CompletableFuture<Void> reloadServer(
			List<IVariantProvider> pProviders,
			ResourceManager pResourceManager,
			Executor pBackgroundExecutor,
			Executor pGameExecutor) {
		clearServerCaches();

		CompletableFuture<Map<ResourceLocation, EntityCache>> variants = loadVariants(pBackgroundExecutor, pResourceManager, pProviders);

		return CompletableFuture.allOf(variants)
				.thenRunAsync(() -> {
					ResourceCache.VARIANTS = variants.join();

					System.out.println("Variants Cache: " + ResourceCache.VARIANTS);
				}, pGameExecutor);
	}


	public static void clearServerCaches() {
		ResourceCache.VARIANTS = Collections.emptyMap();
	}
}
