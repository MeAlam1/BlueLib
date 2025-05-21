// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.VariantLoader;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ReloadHandler {

	private static IVariantProvider provider;

	public static void setProvider(IVariantProvider pVariantProvider) {
		provider = pVariantProvider;
	}

	public static void onServerStart(MinecraftServer pServer) {
		if (provider == null) return;
		
		BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
		BlueLibConstants.server = pServer;
		ReloadHandler.LoadEntityVariants(pServer);
		BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded"), true);
	}

	public static void onReload(MinecraftServer pServer, CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
		if (provider == null) return;
		
		ReloadHandler.LoadEntityVariants(pServer);
		BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.reloaded"), true);
	}
	
	public static void LoadEntityVariants(MinecraftServer pServer) {
		List<String> entityNames = provider.getEntityNames();
		String basePath = provider.getBasePath();

		for (String entityName : entityNames) {
			String folderPath = basePath + entityName;
			VariantLoader.loadVariants(folderPath, pServer, entityName);
			BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded.entity", entityName), true);
		}
	}
}
