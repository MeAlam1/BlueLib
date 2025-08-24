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

	public EntityMoLang(@NotNull Supplier<Entity> pEntitySup) {
		Entity entity = pEntitySup.get();
		setVariable("level", entity.level());
		setVariable("get_id", entity.getId());
		setVariable("get_uuid", entity.getUUID());
		setVariable("get_type", entity.getType().toString());
		setVariable("is_custom_name_visible", entity.isCustomNameVisible());

		setVariable("get_x", entity.getX());
		setVariable("get_y", entity.getY());
		setVariable("get_z", entity.getZ());
		setVariable("get_pos", entity.position());
		setVariable("get_yaw", entity.getYRot());
		setVariable("get_pitch", entity.getXRot());

		setVariable("get_eye_height", entity.getEyeHeight());
		setVariable("get_eye_y", entity.getEyeY());
		setVariable("get_eye_pos", entity.getEyePosition());

		setVariable("get_movement", entity.getDeltaMovement());
		setVariable("get_bounding_box", entity.getBoundingBox());
		setVariable("get_block_pos", entity.blockPosition());
		setVariable("get_chunk_pos", entity.chunkPosition());

		setVariable("get_vehicle", entity.getVehicle());
		setVariable("get_passengers", entity.getPassengers());

		setVariable("on_ground", entity.onGround());
		setVariable("is_in_water", entity.isInWater());
		setVariable("is_in_lava", entity.isInLava());
		setVariable("is_on_fire", entity.isOnFire());
		setVariable("is_invisible", entity.isInvisible());
		setVariable("is_sprinting", entity.isSprinting());

		setVariable("get_width", entity.getBbWidth());
		setVariable("get_height", entity.getBbHeight());

		setVariable("is_removed", entity.isRemoved());
		setVariable("is_pushable", entity.isPushable());
		setVariable("is_no_gravity", entity.isNoGravity());
		setVariable("has_glowing_tag", entity.hasGlowingTag());

		setVariable("get_ticks_frozen", entity.getTicksFrozen());
	}
}
