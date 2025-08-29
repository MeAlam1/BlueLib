/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.event.IEventProxy;
import software.bluelib.internal.BlueResource;
import software.bluelib.platform.IPlatformClient;
import software.bluelib.platform.IPlatformHelper;
import software.bluelib.platform.IRegistryHelper;

public class BlueLibConstants implements BuildDetails {

	public static void init() {}

	@NotNull
	public static <T> T load(@NotNull Class<T> pClazz) {
		return ServiceLoader.load(pClazz)
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Failed to load service for " + pClazz.getName()));
	}

	@NotNull
	public static <T> ServiceLoader<T> loadAll(@NotNull Class<T> pClazz) {
		return ServiceLoader.load(pClazz);
	}

	@NotNull
	public static final Logger LOGGER = Logger.getLogger(BlueLibConstants.MOD_NAME);

	@NotNull
	public static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

	@NotNull
	public static final String MOD_ID = "bluelib";

	@NotNull
	public static final Supplier<DataComponentType<Long>> STACK_ANIMATABLE_ID_COMPONENT = PlatformHelper.REGISTRY.registerDataComponent("stack_animatable_id", builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));

	@NotNull
	public static final String MOD_NAME = "BlueLib";

	@NotNull
	public static final String VERSION = "2.3.6";

	@Nullable
	public static MinecraftServer server;

	@Override
	public @NotNull String getModId() {
		return MOD_ID;
	}

	@Override
	public @NotNull String getVersion() {
		return VERSION;
	}

	@Override
	public boolean displayWarning() {
		return false;
	}

	public static class BlueLoader {

		@NotNull
		public static final ResourceLocation RELOAD_LISTENER_ID = BlueResource.resource("models_animations");
		@NotNull
		public static final ResourceLocation CONTROLLERS_PATH = BlueResource.resource("controller");
		@NotNull
		public static final ResourceLocation ANIMATIONS_PATH = BlueResource.resource("animation");
		@NotNull
		public static final ResourceLocation MODELS_PATH = BlueResource.resource("model");
		@NotNull
		public static final Pattern SUFFIX_STRIPPER = Pattern.compile("((\\.geo)|((\\.animation)s?)|(\\.controller))?(\\.json)$");
		@NotNull
		public static final Pattern PREFIX_STRIPPER = Pattern.compile("^(bluelib/)((animations/)|(models/)|(controllers/))?");
		@NotNull
		public static final List<String> SKIPPED_NAMESPACES = List.of("minecraft", "neoforge");
	}

	public static class PlatformHelper {

		@NotNull
		public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

		@NotNull
		public static final IEventProxy EVENT_PROXY = load(IEventProxy.class);

		@NotNull
		public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

		@NotNull
		public static final IPlatformClient ITEM_RENDERING = load(IPlatformClient.class);
	}
}
