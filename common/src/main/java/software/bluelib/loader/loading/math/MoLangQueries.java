/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.loading.math;

import com.google.common.collect.Streams;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bluelib.client.utils.PlayerUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.loading.math.value.Variable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToDoubleFunction;

public final class MoLangQueries {

	public static final String ACTOR_COUNT = "q.actor_count";
	public static final String ANIM_TIME = "q.anim_time";
	public static final String BLOCKING = "q.blocking";
	public static final String BLOCK_STATE = "q.block_state";
	public static final String BODY_X_ROTATION = "q.body_x_rotation";
	public static final String BODY_Y_ROTATION = "q.body_y_rotation";
	public static final String CAN_CLIMB = "q.can_climb";
	public static final String CAN_FLY = "q.can_fly";
	public static final String CAN_SWIM = "q.can_swim";
	public static final String CAN_WALK = "q.can_walk";
	public static final String CARDINAL_FACING = "q.cardinal_facing";
	public static final String CARDINAL_FACING_2D = "q.cardinal_facing_2d";
	public static final String CARDINAL_PLAYER_FACING = "q.cardinal_player_facing";
	public static final String CONTROLLER_SPEED = "q.controller_speed";
	public static final String DAY = "q.day";
	public static final String DEATH_TICKS = "q.death_ticks";
	public static final String DISTANCE_FROM_CAMERA = "q.distance_from_camera";
	public static final String EQUIPMENT_COUNT = "q.equipment_count";
	public static final String FRAME_ALPHA = "q.frame_alpha";
	public static final String GET_ACTOR_INFO_ID = "q.get_actor_info_id";
	public static final String GROUND_SPEED = "q.ground_speed";
	public static final String HAS_CAPE = "q.has_cape";
	public static final String HAS_COLLISION = "q.has_collision";
	public static final String HAS_GRAVITY = "q.has_gravity";
	public static final String HAS_HEAD_GEAR = "q.has_head_gear";
	public static final String HAS_OWNER = "q.has_owner";
	public static final String HAS_PLAYER_RIDER = "q.has_player_rider";
	public static final String HAS_RIDER = "q.has_rider";
	public static final String HEAD_X_ROTATION = "q.head_x_rotation";
	public static final String HEAD_Y_ROTATION = "q.head_y_rotation";
	public static final String HEALTH = "q.health";
	public static final String HURT_TIME = "q.hurt_time";
	public static final String INVULNERABLE_TICKS = "q.invulnerable_ticks";
	public static final String IS_ALIVE = "q.is_alive";
	public static final String IS_ANGRY = "q.is_angry";
	public static final String IS_BABY = "q.is_baby";
	public static final String IS_BREATHING = "q.is_breathing";
	public static final String IS_ENCHANTED = "q.is_enchanted";
	public static final String IS_FIRE_IMMUNE = "q.is_fire_immune";
	public static final String IS_FIRST_PERSON = "q.is_first_person";
	public static final String IS_INVISIBLE = "q.is_invisible";
	public static final String IS_IN_CONTACT_WITH_WATER = "q.is_in_contact_with_water";
	public static final String IS_IN_LAVA = "q.is_in_lava";
	public static final String IS_IN_WATER = "q.is_in_water";
	public static final String IS_IN_WATER_OR_RAIN = "q.is_in_water_or_rain";
	public static final String IS_LEASHED = "q.is_leashed";
	public static final String IS_MOVING = "q.is_moving";
	public static final String IS_ON_FIRE = "q.is_on_fire";
	public static final String IS_ON_GROUND = "q.is_on_ground";
	public static final String IS_POWERED = "q.is_powered";
	public static final String IS_RIDING = "q.is_riding";
	public static final String IS_SADDLED = "q.is_saddled";
	public static final String IS_SILENT = "q.is_silent";
	public static final String IS_SLEEPING = "q.is_sleeping";
	public static final String IS_SNEAKING = "q.is_sneaking";
	public static final String IS_SPRINTING = "q.is_sprinting";
	public static final String IS_STACKABLE = "q.is_stackable";
	public static final String IS_SWIMMING = "q.is_swimming";
	public static final String IS_USING_ITEM = "q.is_using_item";
	public static final String IS_WALL_CLIMBING = "q.is_wall_climbing";
	public static final String ITEM_MAX_USE_DURATION = "q.item_max_use_duration";
	public static final String LIFE_TIME = "q.life_time";
	public static final String MAIN_HAND_ITEM_MAX_DURATION = "q.main_hand_item_max_duration";
	public static final String MAIN_HAND_ITEM_USE_DURATION = "q.main_hand_item_use_duration";
	public static final String MAX_DURABILITY = "q.max_durability";
	public static final String MAX_HEALTH = "q.max_health";
	public static final String MOON_BRIGHTNESS = "q.moon_brightness";
	public static final String MOON_PHASE = "q.moon_phase";
	public static final String MOVEMENT_DIRECTION = "q.movement_direction";
	public static final String PLAYER_LEVEL = "q.player_level";
	public static final String REMAINING_DURABILITY = "q.remaining_durability";
	public static final String RIDER_BODY_X_ROTATION = "q.rider_body_x_rotation";
	public static final String RIDER_BODY_Y_ROTATION = "q.rider_body_y_rotation";
	public static final String RIDER_HEAD_X_ROTATION = "q.rider_head_x_rotation";
	public static final String RIDER_HEAD_Y_ROTATION = "q.rider_head_y_rotation";
	public static final String SCALE = "q.scale";
	public static final String SLEEP_ROTATION = "q.sleep_rotation";
	public static final String TIME_OF_DAY = "q.time_of_day";
	public static final String TIME_STAMP = "q.time_stamp";
	public static final String VERTICAL_SPEED = "q.vertical_speed";
	public static final String YAW_SPEED = "q.yaw_speed";

	private static final Map<String, Variable> VARIABLES = new ConcurrentHashMap<>();
	private static Actor<?> ACTOR = null;

	static {
		setDefaultQueryValues();
	}

	public static boolean isExistingVariable(String pName) {
		return VARIABLES.containsKey(pName);
	}

	static void registerVariable(Variable pVariable) {
		VARIABLES.put(pVariable.name(), pVariable);
	}

	static Variable getVariableFor(String pName) {
		return VARIABLES.computeIfAbsent(applyPrefixAliases(pName, "q.", "q."), key -> new Variable(key, 0));
	}

	private static String applyPrefixAliases(String pText, String pProperName, String... pAliases) {
		for (String alias : pAliases) {
			if (pText.startsWith(alias))
				return pProperName + pText.substring(alias.length());
		}

		return pText;
	}

	public static void updateActor(AnimationState<? extends BlueAnimatable> pAnimationState, double pAnimTime) {
		ACTOR = new Actor<>(pAnimationState, pAnimationState.getAnimatable(), pAnimTime, Minecraft.getInstance(), Minecraft.getInstance().level);
	}

	public static void clearActor() {
		ACTOR = null;
	}

	public record Actor<T>(AnimationState<? extends BlueAnimatable> animationState, T animatable, double animTime,
	                       Minecraft mc, Level level) {
	}

	public static <T> void setActorVariable(String pName, ToDoubleFunction<Actor<T>> pValue) {
		getVariableFor(pName).set(() -> pValue.applyAsDouble((Actor) getActor()));
	}

	private static Actor<?> getActor() {
		return ACTOR;
	}

	private static void setDefaultQueryValues() {
		getVariableFor("PI").set(Math.PI);
		getVariableFor("E").set(Math.E);
		setActorVariable(CONTROLLER_SPEED, actor -> actor.animationState.getController().getAnimationSpeed());
		setActorVariable(CARDINAL_PLAYER_FACING, actor -> actor.mc.player.getDirection().ordinal());
		setActorVariable(DAY, actor -> actor.level.getGameTime() / 24000d);
		setActorVariable(FRAME_ALPHA, actor -> actor.animationState().getPartialTick());
		setActorVariable(HAS_CAPE, actor -> actor.mc.player.getSkin().capeTexture() != null ? 1 : 0);
		setActorVariable(IS_FIRST_PERSON, actor -> actor.mc.options.getCameraType() == CameraType.FIRST_PERSON ? 1 : 0);
		setActorVariable(LIFE_TIME, actor -> actor.animTime / 20d);
		setActorVariable(MOON_BRIGHTNESS, actor -> actor.level.getMoonBrightness());
		setActorVariable(MOON_PHASE, actor -> actor.level.getMoonPhase());
		setActorVariable(PLAYER_LEVEL, actor -> actor.mc.player.experienceLevel);
		setActorVariable(TIME_OF_DAY, actor -> actor.level.getDayTime() / 24000f);
		setActorVariable(TIME_STAMP, actor -> actor.mc.level.getGameTime());

		setDefaultBlockEntityQueryValues();
		setDefaultEntityQueryValues();
		setDefaultLivingEntityQueryValues();
		setDefaultMobQueryValues();
		setDefaultItemQueryValues();
	}

	private static void setDefaultBlockEntityQueryValues() {
		MoLangQueries.<BlockEntity>setActorVariable(BLOCK_STATE, actor -> actor.animatable.getBlockState().getBlock().getStateDefinition().getPossibleStates().indexOf(actor.animatable.getBlockState()));
	}

	private static void setDefaultEntityQueryValues() {
		MoLangQueries.<Entity>setActorVariable(BODY_X_ROTATION, actor -> actor.animatable instanceof LivingEntity ? 0 : actor.animatable.getViewXRot(actor.animationState.getPartialTick()));
		MoLangQueries.<Entity>setActorVariable(BODY_Y_ROTATION, actor -> actor.animatable instanceof LivingEntity living ? Mth.lerp(actor.animationState.getPartialTick(), living.yBodyRotO, living.yBodyRot) : actor.animatable.getViewYRot(actor.animationState.getPartialTick()));
		MoLangQueries.<Entity>setActorVariable(CARDINAL_FACING, actor -> actor.animatable.getDirection().get3DDataValue());
		MoLangQueries.<Entity>setActorVariable(CARDINAL_FACING_2D, actor -> {
			int directionId = actor.animatable.getDirection().get3DDataValue();

			return directionId < 2 ? 6 : directionId;
		});
		MoLangQueries.<Entity>setActorVariable(DISTANCE_FROM_CAMERA, actor -> actor.mc.gameRenderer.getMainCamera().getPosition().distanceTo(actor.animatable.position()));
		MoLangQueries.<Entity>setActorVariable(GET_ACTOR_INFO_ID, actor -> actor.animatable.getId());
		MoLangQueries.<Entity>setActorVariable(HAS_COLLISION, actor -> !actor.animatable.noPhysics ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(HAS_GRAVITY, actor -> !actor.animatable.isNoGravity() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(HAS_OWNER, actor -> actor.animatable instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(HAS_PLAYER_RIDER, actor -> actor.animatable.hasPassenger(Player.class::isInstance) ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(HAS_RIDER, actor -> actor.animatable.isVehicle() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_ALIVE, actor -> actor.animatable.isAlive() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_ANGRY, actor -> actor.animatable instanceof NeutralMob neutralMob && neutralMob.isAngry() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_BREATHING, actor -> actor.animatable.getAirSupply() >= actor.animatable.getMaxAirSupply() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_FIRE_IMMUNE, actor -> actor.animatable.getType().fireImmune() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_INVISIBLE, actor -> actor.animatable.isInvisible() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_IN_CONTACT_WITH_WATER, actor -> actor.animatable.isInWaterRainOrBubble() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_IN_LAVA, actor -> actor.animatable.isInLava() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_IN_WATER, actor -> actor.animatable.isInWater() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_IN_WATER_OR_RAIN, actor -> actor.animatable.isInWaterOrRain() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_LEASHED, actor -> actor.animatable instanceof Leashable leashable && leashable.isLeashed() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_MOVING, actor -> actor.animationState.isMoving() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_ON_FIRE, actor -> actor.animatable.isOnFire() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_ON_GROUND, actor -> actor.animatable.onGround() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_POWERED, actor -> actor.animatable instanceof PowerableMob powerable && powerable.isPowered() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_RIDING, actor -> actor.animatable.isPassenger() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_SADDLED, actor -> actor.animatable instanceof Saddleable saddleable && saddleable.isSaddled() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_SILENT, actor -> actor.animatable.isSilent() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_SNEAKING, actor -> actor.animatable.isCrouching() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_SPRINTING, actor -> actor.animatable.isSprinting() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(IS_SWIMMING, actor -> actor.animatable.isSwimming() ? 1 : 0);
		MoLangQueries.<Entity>setActorVariable(MOVEMENT_DIRECTION, actor -> actor.animationState.isMoving() ? Direction.getNearest(actor.animatable.getDeltaMovement()).get3DDataValue() : 6);
		MoLangQueries.<Entity>setActorVariable(RIDER_BODY_X_ROTATION, actor -> actor.animatable.isVehicle() ? actor.animatable.getFirstPassenger() instanceof LivingEntity ? 0 : actor.animatable.getFirstPassenger().getViewXRot(actor.animationState.getPartialTick()) : 0);
		MoLangQueries.<Entity>setActorVariable(RIDER_BODY_Y_ROTATION, actor -> actor.animatable.isVehicle() ? actor.animatable.getFirstPassenger() instanceof LivingEntity living ? Mth.lerp(actor.animationState.getPartialTick(), living.yBodyRotO, living.yBodyRot) : actor.animatable.getFirstPassenger().getViewYRot(actor.animationState.getPartialTick()) : 0);
		MoLangQueries.<Entity>setActorVariable(RIDER_HEAD_X_ROTATION, actor -> actor.animatable.getFirstPassenger() instanceof LivingEntity living ? living.getViewXRot(actor.animationState.getPartialTick()) : 0);
		MoLangQueries.<Entity>setActorVariable(RIDER_HEAD_Y_ROTATION, actor -> actor.animatable.getFirstPassenger() instanceof LivingEntity living ? living.getViewYRot(actor.animationState.getPartialTick()) : 0);
		MoLangQueries.<Entity>setActorVariable(VERTICAL_SPEED, actor -> actor.animatable.getDeltaMovement().y);
		MoLangQueries.<Entity>setActorVariable(YAW_SPEED, actor -> actor.animatable.getYRot() - actor.animatable.yRotO);
	}

	private static void setDefaultLivingEntityQueryValues() {
		MoLangQueries.<LivingEntity>setActorVariable(BLOCKING, actor -> actor.animatable.isBlocking() ? 1 : 0);
		MoLangQueries.<LivingEntity>setActorVariable(DEATH_TICKS, actor -> actor.animatable.deathTime == 0 ? 0 : actor.animatable.deathTime + actor.animationState.getPartialTick());
		MoLangQueries.<LivingEntity>setActorVariable(EQUIPMENT_COUNT, actor -> Streams.stream(actor.animatable.getArmorSlots()).filter(stack -> !stack.isEmpty()).count());
		MoLangQueries.<LivingEntity>setActorVariable(GROUND_SPEED, actor -> actor.animatable.getDeltaMovement().horizontalDistance());
		MoLangQueries.<LivingEntity>setActorVariable(HAS_HEAD_GEAR, actor -> !actor.animatable.getItemBySlot(EquipmentSlot.HEAD).isEmpty() ? 1 : 0);
		MoLangQueries.<LivingEntity>setActorVariable(HEAD_X_ROTATION, actor -> actor.animatable.getViewXRot(actor.animationState.getPartialTick()));
		MoLangQueries.<LivingEntity>setActorVariable(HEAD_Y_ROTATION, actor -> actor.animatable.getViewYRot(actor.animationState.getPartialTick()));
		MoLangQueries.<LivingEntity>setActorVariable(HEALTH, actor -> actor.animatable.getHealth());
		MoLangQueries.<LivingEntity>setActorVariable(HURT_TIME, actor -> actor.animatable.hurtTime == 0 ? 0 : actor.animatable.hurtTime - actor.animationState.getPartialTick());
		MoLangQueries.<LivingEntity>setActorVariable(INVULNERABLE_TICKS, actor -> actor.animatable.invulnerableTime == 0 ? 0 : actor.animatable.invulnerableTime - actor.animationState.getPartialTick());
		MoLangQueries.<LivingEntity>setActorVariable(IS_BABY, actor -> actor.animatable.isBaby() ? 1 : 0);
		MoLangQueries.<LivingEntity>setActorVariable(IS_SLEEPING, actor -> actor.animatable.isSleeping() ? 1 : 0);
		MoLangQueries.<LivingEntity>setActorVariable(IS_USING_ITEM, actor -> actor.animatable.isUsingItem() ? 1 : 0);
		MoLangQueries.<LivingEntity>setActorVariable(IS_WALL_CLIMBING, actor -> actor.animatable.onClimbable() ? 1 : 0);
		MoLangQueries.<LivingEntity>setActorVariable(MAIN_HAND_ITEM_MAX_DURATION, actor -> actor.animatable.getMainHandItem().getUseDuration(actor.animatable));
		MoLangQueries.<LivingEntity>setActorVariable(MAIN_HAND_ITEM_USE_DURATION, actor -> actor.animatable.getUsedItemHand() == InteractionHand.MAIN_HAND ? actor.animatable.getTicksUsingItem() / 20d + actor.animationState.getPartialTick() : 0);
		MoLangQueries.<LivingEntity>setActorVariable(MAX_HEALTH, actor -> actor.animatable.getMaxHealth());
		MoLangQueries.<LivingEntity>setActorVariable(SCALE, actor -> actor.animatable.getScale());
		MoLangQueries.<LivingEntity>setActorVariable(SLEEP_ROTATION, actor -> Optional.ofNullable(actor.animatable.getBedOrientation()).map(Direction::toYRot).orElse(0f));
	}

	private static void setDefaultMobQueryValues() {
		MoLangQueries.<Mob>setActorVariable(CAN_CLIMB, actor -> !actor.animatable.isNoAi() && actor.animatable.getNavigation() instanceof WallClimberNavigation ? 1 : 0);
		MoLangQueries.<Mob>setActorVariable(CAN_FLY, actor -> !actor.animatable.isNoAi() && actor.animatable.getNavigation() instanceof FlyingPathNavigation ? 1 : 0);
		MoLangQueries.<Mob>setActorVariable(CAN_SWIM, actor -> !actor.animatable.isNoAi() && actor.animatable.getNavigation() instanceof WaterBoundPathNavigation || actor.animatable.getNavigation() instanceof AmphibiousPathNavigation ? 1 : 0);
		MoLangQueries.<Mob>setActorVariable(CAN_WALK, actor -> !actor.animatable.isNoAi() && actor.animatable.getNavigation() instanceof GroundPathNavigation || actor.animatable.getNavigation() instanceof AmphibiousPathNavigation ? 1 : 0);
	}

	private static void setDefaultItemQueryValues() {
		MoLangQueries.<Item>setActorVariable(IS_ENCHANTED, actor -> actor.animationState.getData(DataTickets.ITEMSTACK).isEnchanted() ? 1 : 0);
		MoLangQueries.<Item>setActorVariable(IS_STACKABLE, actor -> actor.animationState.getData(DataTickets.ITEMSTACK).isStackable() ? 1 : 0);
		MoLangQueries.<Item>setActorVariable(ITEM_MAX_USE_DURATION, actor -> actor.animationState.getData(DataTickets.ITEMSTACK).getUseDuration(PlayerUtils.getClientPlayer()));
		MoLangQueries.<Item>setActorVariable(MAX_DURABILITY, actor -> actor.animationState.getData(DataTickets.ITEMSTACK).getMaxDamage());
		MoLangQueries.<Item>setActorVariable(REMAINING_DURABILITY, actor -> {
			ItemStack stack = actor.animationState.getData(DataTickets.ITEMSTACK);

			return stack.isDamageableItem() ? stack.getMaxDamage() - stack.getDamageValue() : 1;
		});
	}
}
