/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.event.IEventProxy;
import software.bluelib.platform.IPlatformHelper;
import software.bluelib.platform.IRegistryHelper;

public class BlueLibConstants {

    private BlueLibConstants() {}

    public static void init() {}

    @NotNull
    public static <T> T load(@NotNull Class<T> pClazz) {
        return ServiceLoader.load(pClazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + pClazz.getName()));
    }

    @NotNull
    public static final Logger LOGGER = Logger.getLogger(BlueLibConstants.MOD_NAME);

    @NotNull
    public static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    @NotNull
    public static final String MOD_ID = "bluelib";

    @NotNull
    public static final String MOD_NAME = "BlueLib";

    @Nullable
    public static MinecraftServer server;

    public static class BlueLoader {

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
    }
}
