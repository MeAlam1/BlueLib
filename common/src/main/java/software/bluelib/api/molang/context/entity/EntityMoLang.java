package software.bluelib.api.molang.context.entity;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import software.bluelib.api.molang.context.BaseMoLangContext;

/* TODO:
 * All Getters need Setters
 * All Getters need an is method like get_x -> is_x(10) to check if the value is equal to the given value
 * Add Null Checks to all like with get_vehicle
 * Add:
 * AbstractVillager
 * BlockAttachedEntity
 * HangingEntity
 * VehicleEntity
 * Monster
 * AbstractIllager
 * Raider
 * PatrollingMonster
 */
public class EntityMoLang extends BaseMoLangContext {

	public EntityMoLang(Entity pEntity) {
		if (pEntity == null) return;

		setup_base_entity(pEntity);

		if (pEntity instanceof LivingEntity pLivingEntity) {
			setup_living_entity(pLivingEntity);
			if (pEntity instanceof ArmorStand pArmorStand) {
				setup_armor_stand(pArmorStand);
			}
			if (pEntity instanceof Player player) {
				setup_player(player);
			}
			if (pEntity instanceof Mob pMob) {
				setup_mob(pMob);
				if (pEntity instanceof PathfinderMob pPathfinder) {
					setup_pathfinder_mob(pPathfinder);
					if (pEntity instanceof AgeableMob pAgeable) {
						setup_ageable_mob(pAgeable);
						if (pEntity instanceof Villager pVillager) {
							setup_villager(pVillager);
						}
						if (pEntity instanceof Animal pAnimal) {
							setup_animal(pAnimal);
							if (pEntity instanceof Bee pBee) {
								setup_bee(pBee);
							}
							if (pEntity instanceof AbstractHorse pHorse) {
								setup_abstract_horse(pHorse);
							}
							if (pEntity instanceof TamableAnimal pTamable) {
								setup_tameable_animal(pTamable);
								if (pEntity instanceof Wolf pWolf) {
									setup_wolf(pWolf);
								}
							}
						}
					}
				}
			}
		}

	}

	// ====================== ENTITY ======================
	private void setup_base_entity(Entity pEntity) {
		setVariable("level", pEntity.level());
		setVariable("get_id", pEntity.getId());
		setVariable("get_uuid", pEntity.getUUID());
		setVariable("get_type", pEntity.getType().toString());
		setVariable("is_custom_name_visible", pEntity.isCustomNameVisible());

		setVariable("get_x", pEntity.getX());
		setVariable("get_y", pEntity.getY());
		setVariable("get_z", pEntity.getZ());
		setVariable("get_pos", pEntity.position());
		setVariable("get_yaw", pEntity.getYRot());
		setVariable("get_pitch", pEntity.getXRot());

		setVariable("get_eye_height", pEntity.getEyeHeight());
		setVariable("get_eye_y", pEntity.getEyeY());
		setVariable("get_eye_pos", pEntity.getEyePosition());

		setVariable("get_movement", pEntity.getDeltaMovement());
		setVariable("get_bounding_box", pEntity.getBoundingBox());
		setVariable("get_block_pos", pEntity.blockPosition());
		setVariable("get_chunk_pos", pEntity.chunkPosition());

		setVariable("get_vehicle", pEntity.getVehicle());
		setVariable("get_passengers", pEntity.getPassengers());

		setVariable("on_ground", pEntity.onGround());
		setVariable("is_in_water", pEntity.isInWater());
		setVariable("is_in_lava", pEntity.isInLava());
		setVariable("is_on_fire", pEntity.isOnFire());
		setVariable("is_invisible", pEntity.isInvisible());
		setVariable("is_sprinting", pEntity.isSprinting());

		setVariable("get_width", pEntity.getBbWidth());
		setVariable("get_height", pEntity.getBbHeight());

		setVariable("is_removed", pEntity.isRemoved());
		setVariable("is_pushable", pEntity.isPushable());
		setVariable("is_no_gravity", pEntity.isNoGravity());
		setVariable("has_glowing_tag", pEntity.hasGlowingTag());

		setVariable("get_ticks_frozen", pEntity.getTicksFrozen());
	}

	// ====================== LIVING ENTITY ======================
	private void setup_living_entity(LivingEntity pLivingEntity) {
		setVariable("get_health", pLivingEntity.getHealth());
		setVariable("get_max_health", pLivingEntity.getMaxHealth());
		setVariable("get_main_hand_item", pLivingEntity.getMainHandItem());
		setVariable("get_offhand_item", pLivingEntity.getOffhandItem());
		setVariable("get_armor_items", pLivingEntity.getArmorSlots());

		setVariable("is_baby", pLivingEntity.isBaby());
		setVariable("is_sleeping", pLivingEntity.isSleeping());
		setVariable("is_fall_flying", pLivingEntity.isFallFlying());

		setVariable("get_last_hurt_by_entity", pLivingEntity.getLastHurtByMob());
		setVariable("get_last_damage_source", pLivingEntity.getLastDamageSource());
		setVariable("get_active_effects", pLivingEntity.getActiveEffects());
		setVariable("get_use_item", pLivingEntity.getUseItem());

		setVariable("is_using_item", pLivingEntity.isUsingItem());
		setVariable("is_glowing", pLivingEntity.isCurrentlyGlowing());
		setVariable("is_dead_or_dying", pLivingEntity.isDeadOrDying());

		setVariable("get_absorption_amount", pLivingEntity.getAbsorptionAmount());
		setVariable("get_arrow_count", pLivingEntity.getArrowCount());
	}

	// ====================== PLAYER ======================
	private void setup_player(Player pPlayer) {
		setVariable("get_xp", pPlayer.totalExperience);
		setVariable("get_level", pPlayer.experienceLevel);
		setVariable("get_hunger", pPlayer.getFoodData().getFoodLevel());
		setVariable("get_saturation", pPlayer.getFoodData().getSaturationLevel());
		setVariable("is_crouching", pPlayer.isCrouching());
		setVariable("is_swimming", pPlayer.isSwimming());
		setVariable("get_sleep_timer", pPlayer.getSleepTimer());
	}

	// ====================== MOB ======================
	private void setup_mob(Mob pMob) {
		setVariable("get_target", pMob.getTarget());
		setVariable("get_brain", pMob.getBrain());
		setVariable("get_navigation", pMob.getNavigation());
		setVariable("get_look_control", pMob.getLookControl());
		setVariable("get_move_control", pMob.getMoveControl());
		setVariable("get_jump_control", pMob.getJumpControl());
		setVariable("can_attack", pMob.getTarget() == null ? null : pMob.canAttack(pMob.getTarget()));
		setVariable("can_pickup_loot", pMob.canPickUpLoot());
		setVariable("get_leash_holder", pMob.getLeashHolder());
		setVariable("get_no_action_time", pMob.getNoActionTime());
	}

	// ====================== PATHFINDER MOB ======================
	private void setup_pathfinder_mob(PathfinderMob pPathfinderMob) {
		setVariable("is_pathfinding", pPathfinderMob.isPathFinding());
	}

	// ====================== AGEABLE MOB ======================
	private void setup_ageable_mob(AgeableMob pAgeableMob) {
		setVariable("get_age", pAgeableMob.getAge());
		setVariable("can_breed", pAgeableMob.canBreed());
	}

	// ====================== TAMED ANIMAL ======================
	private void setup_tameable_animal(TamableAnimal pTamableAnimal) {
		setVariable("is_tamed", pTamableAnimal.isTame());
		setVariable("get_owner_uuid", pTamableAnimal.getOwnerUUID());
		setVariable("is_in_love", pTamableAnimal.isInLove());
	}

	// ====================== ANIMAL ======================
	private void setup_animal(Animal pAnimal) {
	}

	// ====================== ABSTRACT HORSE ======================
	private void setup_abstract_horse(AbstractHorse pAbstractHorse) {
		setVariable("is_saddled", pAbstractHorse.isSaddled());
		setVariable("get_temper", pAbstractHorse.getTemper());
		setVariable("is_tamed", pAbstractHorse.isTamed());
	}

	// ====================== VILLAGER ======================
	private void setup_villager(Villager pVillager) {
		setVariable("get_villager_profession", pVillager.getVillagerData().getProfession().toString());
		setVariable("get_trade_offers", pVillager.getOffers());
	}

	// ====================== WOLF ======================
	private void setup_wolf(Wolf pWolf) {
		setVariable("is_angry", pWolf.isAngry());
		setVariable("is_wet", pWolf.isWet());
		setVariable("get_collar_color", pWolf.getCollarColor().name());
	}

	// ====================== BEE ======================
	private void setup_bee(Bee pBee) {
		setVariable("has_nectar", pBee.hasNectar());
		setVariable("is_angry", pBee.isAngry());
		setVariable("has_stung", pBee.hasStung());
	}

	// ====================== ARMOR STAND ======================
	private void setup_armor_stand(ArmorStand pArmorStand) {
		setVariable("is_marker", pArmorStand.isMarker());
		setVariable("is_small", pArmorStand.isSmall());
		setVariable("is_show_arms", pArmorStand.isShowArms());
		setVariable("get_head_pose", pArmorStand.getHeadPose());
		setVariable("get_body_pose", pArmorStand.getBodyPose());
		setVariable("get_left_arm_pose", pArmorStand.getLeftArmPose());
		setVariable("get_right_arm_pose", pArmorStand.getRightArmPose());
		setVariable("get_left_leg_pose", pArmorStand.getLeftLegPose());
		setVariable("get_right_leg_pose", pArmorStand.getRightLegPose());
	}
}