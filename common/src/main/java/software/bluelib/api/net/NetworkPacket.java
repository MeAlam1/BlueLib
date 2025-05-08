// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.net;

import java.util.function.Predicate;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;

public interface NetworkPacket<T extends NetworkPacket<T>> extends CustomPacketPayload, Encodable {

    ResourceLocation getId();

    default void sendToPlayer(ServerPlayer pPlayer) {
        NetworkRegistry.sendPacketToPlayer(pPlayer, this);
    }

    default void sendToPlayers(Iterable<ServerPlayer> pPlayers) {
        if (pPlayers.iterator().hasNext()) {
            NetworkRegistry.sendPacketToPlayers(pPlayers, this);
        }
    }

    default void sendToAllPlayers() {
        NetworkRegistry.sendToAllPlayers(this);
    }

    default void sendToServer() {
        NetworkRegistry.sendToServer(this);
    }

    default void sendToPlayersAround(double pX, double pY, double pZ, double pDistance, ResourceKey<Level> pWorldKey, Predicate<ServerPlayer> pExclusionCondition) {
        var server = BlueLibConstants.PlatformHelper.PLATFORM.getServer();
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (pExclusionCondition.test(player)) continue;
            if (!player.level().dimension().equals(pWorldKey)) continue;

            double xDiff = pX - player.getX();
            double yDiff = pY - player.getY();
            double zDiff = pZ - player.getZ();
            if (xDiff * xDiff + yDiff * yDiff + zDiff * zDiff < pDistance * pDistance) {
                NetworkRegistry.sendPacketToPlayer(player, this);
            }
        }
    }

    default void sendToPlayersAround(double pX, double pY, double pZ, double pDistance, ResourceKey<Level> pWorldKey) {
        sendToPlayersAround(pX, pY, pZ, pDistance, pWorldKey, player -> false);
    }

    @Override
    default @NotNull Type<T> type() {
        return new Type<>(getId());
    }
}
