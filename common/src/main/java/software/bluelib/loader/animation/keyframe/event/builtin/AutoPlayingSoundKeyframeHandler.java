package software.bluelib.loader.animation.keyframe.event.builtin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.keyframe.event.SoundKeyframeEvent;
import software.bluelib.loader.util.ClientUtil;


public class AutoPlayingSoundKeyframeHandler<A extends GeoAnimatable> implements AnimationController.SoundKeyframeHandler<A> {
    @Override
    public void handle(SoundKeyframeEvent<A> event) {
        String[] segments = event.getKeyframeData().getSound().split("\\|");
        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.read(segments[0]).getOrThrow());

        if (sound != null) {
            Entity entity = event.getAnimatable() instanceof Entity e ? e : null;
            Vec3 position = entity != null ? entity.position() : event.getAnimatable() instanceof BlockEntity blockEntity ? blockEntity.getBlockPos().getCenter() : null;

            if (position != null) {
                float volume = segments.length > 1 ? Float.parseFloat(segments[1]) : 1;
                float pitch = segments.length > 2 ? Float.parseFloat(segments[2]) : 1;
                SoundSource source = entity == null ? SoundSource.BLOCKS : entity instanceof Enemy ? SoundSource.HOSTILE : SoundSource.NEUTRAL;

                ClientUtil.getLevel().playLocalSound(position.x, position.y, position.z, sound, source, volume, pitch, false);
            }
        }
    }
}
