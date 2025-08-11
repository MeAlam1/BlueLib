/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class EntityMoLang extends BaseMoLangContext {

	public EntityMoLang(@NotNull Supplier<Entity> pEntity) {
		setVariable("level", pEntity.get().level());
		setVariable("get_id", pEntity.get().getId());
		setVariable("get_uuid", pEntity.get().getUUID());
		setVariable("get_type", pEntity.get().getType().toString());
		setVariable("is_custom_name_visible", pEntity.get().isCustomNameVisible());

		setVariable("get_x", pEntity.get().getX());
		setVariable("get_y", pEntity.get().getY());
		setVariable("get_z", pEntity.get().getZ());
		setVariable("get_pos", pEntity.get().position());
		setVariable("get_yaw", pEntity.get().getYRot());
		setVariable("get_pitch", pEntity.get().getXRot());

		setVariable("get_eye_height", pEntity.get().getEyeHeight());
		setVariable("get_eye_y", pEntity.get().getEyeY());
		setVariable("get_eye_pos", pEntity.get().getEyePosition());

		setVariable("get_movement", pEntity.get().getDeltaMovement());
		setVariable("get_bounding_box", pEntity.get().getBoundingBox());
		setVariable("get_block_pos", pEntity.get().blockPosition());
		setVariable("get_chunk_pos", pEntity.get().chunkPosition());

		setVariable("get_vehicle", pEntity.get().getVehicle());
		setVariable("get_passengers", pEntity.get().getPassengers());

		setVariable("on_ground", pEntity.get().onGround());
		setVariable("is_in_water", pEntity.get().isInWater());
		setVariable("is_in_lava", pEntity.get().isInLava());
		setVariable("is_on_fire", pEntity.get().isOnFire());
		setVariable("is_invisible", pEntity.get().isInvisible());
		setVariable("is_sprinting", pEntity.get().isSprinting());

		setVariable("get_width", pEntity.get().getBbWidth());
		setVariable("get_height", pEntity.get().getBbHeight());

		setVariable("is_removed", pEntity.get().isRemoved());
		setVariable("is_pushable", pEntity.get().isPushable());
		setVariable("is_no_gravity", pEntity.get().isNoGravity());
		setVariable("has_glowing_tag", pEntity.get().hasGlowingTag());

		setVariable("get_ticks_frozen", pEntity.get().getTicksFrozen());
	}
}
