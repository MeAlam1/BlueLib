/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.packet;

import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibCommon;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.util.GeckoLibUtil;

public record StopTriggeredSingletonAnimPacket(String syncableId, long instanceId, String controllerName, String animName) implements MultiloaderPacket {

    public static final Type<StopTriggeredSingletonAnimPacket> TYPE = new Type<>(BlueLibCommon.Resource.resource("stop_triggered_singleton_anim"));
    public static final StreamCodec<FriendlyByteBuf, StopTriggeredSingletonAnimPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, StopTriggeredSingletonAnimPacket::syncableId,
            ByteBufCodecs.VAR_LONG, StopTriggeredSingletonAnimPacket::instanceId,
            ByteBufCodecs.STRING_UTF8, StopTriggeredSingletonAnimPacket::controllerName,
            ByteBufCodecs.STRING_UTF8, StopTriggeredSingletonAnimPacket::animName,
            StopTriggeredSingletonAnimPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void receiveMessage(@Nullable Player sender, Consumer<Runnable> workQueue) {
        workQueue.accept(() -> {
            GeoAnimatable animatable = GeckoLibUtil.getSyncedAnimatable(this.syncableId);

            if (animatable != null) {
                AnimatableManager<GeoAnimatable> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(this.instanceId);

                if (animatableManager != null)
                    animatableManager.stopTriggeredAnimation(this.controllerName.isEmpty() ? null : this.controllerName, this.animName.isEmpty() ? null : this.animName);
            }
        });
    }
}
