/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.api.event.IEventProxy;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.platform.IPlatformHelper;
import software.bluelib.platform.IRegistryHelper;

public class BlueLibConstants {

    private BlueLibConstants() {}

    public static <T> T load(Class<T> pClazz) {
        return ServiceLoader.load(pClazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + pClazz.getName()));
    }

    public static final Logger LOGGER = Logger.getLogger(BlueLibConstants.MOD_NAME);

    public static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    public static final String MOD_ID = "bluelib";

    public static final String MOD_NAME = "BlueLib";

    public static MinecraftServer server;

    public static class PlatformHelper {

        public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

        public static final IEventProxy EVENT_PROXY = load(IEventProxy.class);

        public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    }

    public enum ModAPI {
        FABRIC,
        FORGE,
        NEOFORGE
    }

    public interface NetworkManager {

        void sendPacketToPlayer(ServerPlayer player, NetworkPacket<?> packet);

        void sendToServer(NetworkPacket<?> packet);
    }

    public enum Environment {
        CLIENT,
        SERVER
    }
}
