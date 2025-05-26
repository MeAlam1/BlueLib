package software.bluelib.loader.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.GeckoLibConstants;
import software.bluelib.loader.animatable.GeoEntity;
import software.bluelib.loader.animatable.GeoReplacedEntity;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.loader.util.RenderUtil;

import java.util.function.Consumer;

public record StopTriggeredEntityAnimPacket(int entityId, boolean isReplacedEntity, String controllerName, String animName) implements MultiloaderPacket {
    public static final Type<StopTriggeredEntityAnimPacket> TYPE = new Type<>(GeckoLibConstants.id("stop_triggered_entity_anim"));
    public static final StreamCodec<FriendlyByteBuf, StopTriggeredEntityAnimPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, StopTriggeredEntityAnimPacket::entityId,
            ByteBufCodecs.BOOL, StopTriggeredEntityAnimPacket::isReplacedEntity,
            ByteBufCodecs.STRING_UTF8, StopTriggeredEntityAnimPacket::controllerName,
            ByteBufCodecs.STRING_UTF8, StopTriggeredEntityAnimPacket::animName,
            StopTriggeredEntityAnimPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void receiveMessage(@Nullable Player sender, Consumer<Runnable> workQueue) {
        workQueue.accept(() -> {
            Entity entity = ClientUtil.getLevel().getEntity(this.entityId);

            if (entity == null)
                return;

            if (!this.isReplacedEntity) {
                if (entity instanceof GeoEntity geoEntity)
                    geoEntity.stopTriggeredAnim(this.controllerName.isEmpty() ? null : this.controllerName, this.animName.isEmpty() ? null : this.animName);

                return;
            }

            if (RenderUtil.getReplacedAnimatable(entity.getType()) instanceof GeoReplacedEntity replacedEntity)
                replacedEntity.stopTriggeredAnim(entity, this.controllerName.isEmpty() ? null : this.controllerName, this.animName.isEmpty() ? null : this.animName);
        });
    }
}