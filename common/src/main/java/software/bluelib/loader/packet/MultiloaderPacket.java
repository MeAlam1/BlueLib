package software.bluelib.loader.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


public interface MultiloaderPacket extends CustomPacketPayload {
    
    void receiveMessage(@Nullable Player sender, Consumer<Runnable> workQueue);
}
