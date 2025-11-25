/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.handler;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bluelib.client.utils.LevelUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.keyframe.event.SoundKeyframeEvent;

public class AutoPlayingSoundKeyframeHandler<A extends BlueAnimatable> implements AnimationController.SoundKeyframeHandler<A> {

	@Override
	public void handle(@NotNull SoundKeyframeEvent<A> pEvent) {
		String soundData = pEvent.getKeyframeData().getSound();
		if (soundData == null || soundData.isEmpty()) {
			return;
		}

		String[] segments = soundData.split("\\|");
		if (segments.length == 0 || segments[0].isEmpty()) {
			return;
		}

		SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.read(segments[0]).getOrThrow());

		if (LevelUtils.getLevel() == null) {
			return;
		}

		if (sound != null) {
			Entity entity = pEvent.getAnimatable() instanceof Entity e ? e : null;
			Vec3 position = entity != null ? entity.position() : pEvent.getAnimatable() instanceof BlockEntity blockEntity ? blockEntity.getBlockPos().getCenter() : null;

			if (position != null) {
				float volume = 1;
				float pitch = 1;
				try {
					if (segments.length > 1) {
						volume = Float.parseFloat(segments[1]);
					}
					if (segments.length > 2) {
						pitch = Float.parseFloat(segments[2]);
					}
				} catch (NumberFormatException e) {
					// Use default values if parsing fails
				}
				SoundSource source = entity == null ? SoundSource.BLOCKS : entity instanceof Enemy ? SoundSource.HOSTILE : SoundSource.NEUTRAL;

				LevelUtils.getLevel().playLocalSound(position.x, position.y, position.z, sound, source, volume, pitch, false);
			}
		}
	}
}
