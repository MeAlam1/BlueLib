package software.bluelib.api.molang.context;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/* TODO:
	* All Getters need Setters
	* All Getters need an is method like get_x -> is_x(10) to check if the value is equal to the given value
	* Add Null Checks to all like with get_vehicle
	* Refactor Living Entity and Mob into own Methods/SubClasses
 */
public class EntityMoLang extends BaseMoLangContext {

	public EntityMoLang(Entity pEntity) {
		// Basic Entity data
		setVariable("level", pEntity.level());
		setVariable("get_id", pEntity.getId());
		setVariable("get_uuid", pEntity.getUUID());
		setVariable("get_type", pEntity.getType());
		setVariable("is_custom_name_visible", pEntity.isCustomNameVisible());

		// Position and rotation
		setVariable("get_x", pEntity.getX());
		setVariable("get_y", pEntity.getY());
		setVariable("get_z", pEntity.getZ());
		setVariable("get_pos", pEntity.position());
		setVariable("get_yaw", pEntity.getYRot());
		setVariable("get_pitch", pEntity.getXRot());
		setVariable("get_eye_height", pEntity.getEyeHeight());
		setVariable("get_eye_y", pEntity.getEyeY());
		setVariable("get_eye_pos", pEntity.getEyePosition());

		// Movement and physics
		setVariable("get_movement", pEntity.getDeltaMovement());
		setVariable("get_bounding_box", pEntity.getBoundingBox());
		setVariable("get_block_pos", pEntity.blockPosition());
		setVariable("get_chunk_pos", pEntity.chunkPosition());
		setVariable("get_vehicle", pEntity.getVehicle() != null ? pEntity.getVehicle() : null);
		setVariable("get_passengers", pEntity.getPassengers());
		setVariable("on_ground", pEntity.onGround());
		setVariable("is_in_water", pEntity.isInWater());
		setVariable("is_in_lava", pEntity.isInLava());
		setVariable("is_on_fire", pEntity.isOnFire());
		setVariable("is_invisible", pEntity.isInvisible());
		setVariable("is_sprinting", pEntity.isSprinting());

		// Size
		setVariable("get_width", pEntity.getBbWidth());
		setVariable("get_height", pEntity.getBbHeight());

		// LivingEntity-specific getters
		if (pEntity instanceof LivingEntity living) {
			setVariable("get_health", living.getHealth());
			setVariable("get_main_hand_item", living.getMainHandItem());
			setVariable("get_offhand_item", living.getOffhandItem());
			setVariable("get_armor_items", living.getArmorSlots());
			setVariable("is_baby", living.isBaby());
			setVariable("is_sleeping", living.isSleeping());
			setVariable("is_fall_flying", living.isFallFlying());
			setVariable("get_last_hurt_by_entity", living.getLastHurtByMob());
			setVariable("get_last_damage_source", living.getLastDamageSource());
			setVariable("get_active_effects", living.getActiveEffects());
		}

		// Mob-specific getters
		if (pEntity instanceof Mob mob) {
			setVariable("get_target", mob.getTarget());
			setVariable("get_brain", mob.getBrain());
			setVariable("get_navigation", mob.getNavigation());
			setVariable("get_look_control", mob.getLookControl());
			setVariable("get_move_control", mob.getMoveControl());
			setVariable("get_jump_control", mob.getJumpControl());
		}
	}
}
