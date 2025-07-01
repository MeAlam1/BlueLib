package software.bluelib.api.net;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface NetworkManager {

    void sendPacketToPlayer(@NotNull ServerPlayer player, @NotNull NetworkPacket<?> packet);

    void sendToServer(@NotNull NetworkPacket<?> packet);
}
