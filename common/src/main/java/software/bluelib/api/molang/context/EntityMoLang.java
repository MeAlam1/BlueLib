package software.bluelib.api.molang.context;

import java.util.function.Supplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

/*
 * TODO:
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

    public EntityMoLang(Supplier<Entity> pEntity) {
        if (pEntity == null) return;

        setup_base_entity(pEntity);

        Entity entity = pEntity.get();
        if (entity instanceof LivingEntity) {
            Supplier<LivingEntity> livingSupplier = () -> (LivingEntity) pEntity.get();
            setup_living_entity(livingSupplier);

            if (entity instanceof ArmorStand) {
                setup_armor_stand(() -> (ArmorStand) pEntity.get());
            }
            if (entity instanceof Player) {
                setup_player(() -> (Player) pEntity.get());
            }
            if (entity instanceof Mob) {
                Supplier<Mob> mobSupplier = () -> (Mob) pEntity.get();
                setup_mob(mobSupplier);

                if (entity instanceof PathfinderMob) {
                    Supplier<PathfinderMob> pathfinderSupplier = () -> (PathfinderMob) pEntity.get();
                    setup_pathfinder_mob(pathfinderSupplier);

                    if (entity instanceof AgeableMob) {
                        Supplier<AgeableMob> ageableSupplier = () -> (AgeableMob) pEntity.get();
                        setup_ageable_mob(ageableSupplier);

                        if (entity instanceof Villager) {
                            setup_villager(() -> (Villager) pEntity.get());
                        }
                        if (entity instanceof Animal) {
                            Supplier<Animal> animalSupplier = () -> (Animal) pEntity.get();
                            setup_animal(animalSupplier);

                            if (entity instanceof Bee) {
                                setup_bee(() -> (Bee) pEntity.get());
                            }
                            if (entity instanceof AbstractHorse) {
                                setup_abstract_horse(() -> (AbstractHorse) pEntity.get());
                            }
                            if (entity instanceof TamableAnimal) {
                                Supplier<TamableAnimal> tamableSupplier = () -> (TamableAnimal) pEntity.get();
                                setup_tameable_animal(tamableSupplier);

                                if (entity instanceof Wolf) {
                                    setup_wolf(() -> (Wolf) pEntity.get());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ====================== ENTITY ======================
    private void setup_base_entity(Supplier<Entity> pEntity) {
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

    // ====================== LIVING ENTITY ======================
    private void setup_living_entity(Supplier<LivingEntity> pLivingEntity) {
        setVariable("get_health", pLivingEntity.get().getHealth());
        setVariable("get_max_health", pLivingEntity.get().getMaxHealth());
        setVariable("get_main_hand_item", pLivingEntity.get().getMainHandItem());
        setVariable("get_offhand_item", pLivingEntity.get().getOffhandItem());
        setVariable("get_armor_items", pLivingEntity.get().getArmorSlots());

        setVariable("is_baby", pLivingEntity.get().isBaby());
        setVariable("is_sleeping", pLivingEntity.get().isSleeping());
        setVariable("is_fall_flying", pLivingEntity.get().isFallFlying());

        setVariable("get_last_hurt_by_entity", pLivingEntity.get().getLastHurtByMob());
        setVariable("get_last_damage_source", pLivingEntity.get().getLastDamageSource());
        setVariable("get_active_effects", pLivingEntity.get().getActiveEffects());
        setVariable("get_use_item", pLivingEntity.get().getUseItem());

        setVariable("is_using_item", pLivingEntity.get().isUsingItem());
        setVariable("is_glowing", pLivingEntity.get().isCurrentlyGlowing());
        setVariable("is_dead_or_dying", pLivingEntity.get().isDeadOrDying());

        setVariable("get_absorption_amount", pLivingEntity.get().getAbsorptionAmount());
        setVariable("get_arrow_count", pLivingEntity.get().getArrowCount());
    }

    // ====================== PLAYER ======================
    private void setup_player(Supplier<Player> pPlayer) {
        setVariable("get_xp", pPlayer.get().totalExperience);
        setVariable("get_level", pPlayer.get().experienceLevel);
        setVariable("get_hunger", pPlayer.get().getFoodData().getFoodLevel());
        setVariable("get_saturation", pPlayer.get().getFoodData().getSaturationLevel());
        setVariable("is_crouching", pPlayer.get().isCrouching());
        setVariable("is_swimming", pPlayer.get().isSwimming());
        setVariable("get_sleep_timer", pPlayer.get().getSleepTimer());
    }

    // ====================== MOB ======================
    private void setup_mob(Supplier<Mob> pMob) {
        setVariable("get_target", pMob.get().getTarget());
        setVariable("get_brain", pMob.get().getBrain());
        setVariable("get_navigation", pMob.get().getNavigation());
        setVariable("get_look_control", pMob.get().getLookControl());
        setVariable("get_move_control", pMob.get().getMoveControl());
        setVariable("get_jump_control", pMob.get().getJumpControl());
        setVariable("can_attack", pMob.get().getTarget() == null ? null : pMob.get().canAttack(pMob.get().getTarget()));
        setVariable("can_pickup_loot", pMob.get().canPickUpLoot());
        setVariable("get_leash_holder", pMob.get().getLeashHolder());
        setVariable("get_no_action_time", pMob.get().getNoActionTime());
    }

    // ====================== PATHFINDER MOB ======================
    private void setup_pathfinder_mob(Supplier<PathfinderMob> pPathfinderMob) {
        setVariable("is_pathfinding", pPathfinderMob.get().isPathFinding());
    }

    // ====================== AGEABLE MOB ======================
    private void setup_ageable_mob(Supplier<AgeableMob> pAgeableMob) {
        setVariable("get_age", pAgeableMob.get().getAge());
        setVariable("can_breed", pAgeableMob.get().canBreed());
    }

    // ====================== TAMED ANIMAL ======================
    private void setup_tameable_animal(Supplier<TamableAnimal> pTamableAnimal) {
        setVariable("is_tamed", pTamableAnimal.get().isTame());
        setVariable("get_owner_uuid", pTamableAnimal.get().getOwnerUUID());
        setVariable("is_in_love", pTamableAnimal.get().isInLove());
    }

    // ====================== ANIMAL ======================
    private void setup_animal(Supplier<Animal> pAnimal) {}

    // ====================== ABSTRACT HORSE ======================
    private void setup_abstract_horse(Supplier<AbstractHorse> pAbstractHorse) {
        setVariable("is_saddled", pAbstractHorse.get().isSaddled());
        setVariable("get_temper", pAbstractHorse.get().getTemper());
        setVariable("is_tamed", pAbstractHorse.get().isTamed());
    }

    // ====================== VILLAGER ======================
    private void setup_villager(Supplier<Villager> pVillager) {
        setVariable("get_villager_profession", pVillager.get().getVillagerData().getProfession().toString());
    }

    // ====================== WOLF ======================
    private void setup_wolf(Supplier<Wolf> pWolf) {
        setVariable("is_angry", pWolf.get().isAngry());
        setVariable("is_wet", pWolf.get().isWet());
        setVariable("get_collar_color", pWolf.get().getCollarColor().name());
    }

    // ====================== BEE ======================
    private void setup_bee(Supplier<Bee> pBee) {
        setVariable("has_nectar", pBee.get().hasNectar());
        setVariable("is_angry", pBee.get().isAngry());
        setVariable("has_stung", pBee.get().hasStung());
    }

    // ====================== ARMOR STAND ======================
    private void setup_armor_stand(Supplier<ArmorStand> pArmorStand) {
        setVariable("is_marker", pArmorStand.get().isMarker());
        setVariable("is_small", pArmorStand.get().isSmall());
        setVariable("is_show_arms", pArmorStand.get().isShowArms());
        setVariable("get_head_pose", pArmorStand.get().getHeadPose());
        setVariable("get_body_pose", pArmorStand.get().getBodyPose());
        setVariable("get_left_arm_pose", pArmorStand.get().getLeftArmPose());
        setVariable("get_right_arm_pose", pArmorStand.get().getRightArmPose());
        setVariable("get_left_leg_pose", pArmorStand.get().getLeftLegPose());
        setVariable("get_right_leg_pose", pArmorStand.get().getRightLegPose());
    }
}
