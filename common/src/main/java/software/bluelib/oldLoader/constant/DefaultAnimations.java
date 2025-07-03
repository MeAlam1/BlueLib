/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.constant;

import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimationController;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.animation.PlayState;
import software.bluelib.oldLoader.animation.RawAnimation;

public final class DefaultAnimations {

	public static final RawAnimation ITEM_ON_USE = RawAnimation.begin().thenPlay("item.use");

	public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("misc.idle");
	public static final RawAnimation LIVING = RawAnimation.begin().thenLoop("misc.living");
	public static final RawAnimation SPAWN = RawAnimation.begin().thenPlay("misc.spawn");
	public static final RawAnimation DIE = RawAnimation.begin().thenPlay("misc.die");
	public static final RawAnimation INTERACT = RawAnimation.begin().thenPlay("misc.interact");
	public static final RawAnimation DEPLOY = RawAnimation.begin().thenPlay("misc.deploy");
	public static final RawAnimation REST = RawAnimation.begin().thenPlay("misc.rest");
	public static final RawAnimation SIT = RawAnimation.begin().thenPlayAndHold("misc.sit");

	public static final RawAnimation WALK = RawAnimation.begin().thenLoop("move.walk");
	public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("move.swim");
	public static final RawAnimation RUN = RawAnimation.begin().thenLoop("move.run");
	public static final RawAnimation DRIVE = RawAnimation.begin().thenLoop("move.drive");
	public static final RawAnimation FLY = RawAnimation.begin().thenLoop("move.fly");
	public static final RawAnimation CRAWL = RawAnimation.begin().thenLoop("move.crawl");
	public static final RawAnimation JUMP = RawAnimation.begin().thenPlay("move.jump");
	public static final RawAnimation SNEAK = RawAnimation.begin().thenLoop("move.sneak");

	public static final RawAnimation ATTACK_CAST = RawAnimation.begin().thenPlay("attack.cast");
	public static final RawAnimation ATTACK_SWING = RawAnimation.begin().thenPlay("attack.swing");
	public static final RawAnimation ATTACK_THROW = RawAnimation.begin().thenPlay("attack.throw");
	public static final RawAnimation ATTACK_BITE = RawAnimation.begin().thenPlay("attack.bite");
	public static final RawAnimation ATTACK_SLAM = RawAnimation.begin().thenPlay("attack.slam");
	public static final RawAnimation ATTACK_STOMP = RawAnimation.begin().thenPlay("attack.stomp");
	public static final RawAnimation ATTACK_STRIKE = RawAnimation.begin().thenPlay("attack.strike");
	public static final RawAnimation ATTACK_FLYING_ATTACK = RawAnimation.begin().thenPlay("attack.flying_attack");
	public static final RawAnimation ATTACK_SHOOT = RawAnimation.begin().thenPlay("attack.shoot");
	public static final RawAnimation ATTACK_BLOCK = RawAnimation.begin().thenPlay("attack.block");
	public static final RawAnimation ATTACK_CHARGE = RawAnimation.begin().thenPlay("attack.charge");
	public static final RawAnimation ATTACK_CHARGE_END = RawAnimation.begin().thenPlay("attack.charge_end");
	public static final RawAnimation ATTACK_POWERUP = RawAnimation.begin().thenPlay("attack.powerup");

	public static <T extends BlueAnimatable> AnimationController<T> basicPredicateController(T animatable, RawAnimation optionA, RawAnimation optionB, BiFunction<T, AnimationState<T>, Boolean> predicate) {
		return new AnimationController<T>(animatable, "Generic", 10, state -> {
			Boolean result = predicate.apply(animatable, state);

			if (result == null)
				return PlayState.STOP;

			return state.setAndContinue(result ? optionA : optionB);
		});
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericLivingController(T animatable) {
		return new AnimationController<>(animatable, "Living", 10, state -> state.setAndContinue(LIVING));
	}

	public static <T extends LivingEntity & BlueAnimatable> AnimationController<T> genericDeathController(T animatable) {
		return new AnimationController<>(animatable, "Death", 0, state -> state.getAnimatable().isDeadOrDying() ? state.setAndContinue(DIE) : PlayState.STOP);
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericIdleController(T animatable) {
		return new AnimationController<T>(animatable, "Idle", 10, state -> state.setAndContinue(IDLE));
	}

	public static <T extends BlueAnimatable> AnimationController<T> getSpawnController(T animatable, Function<AnimationState<T>, Object> objectSupplier, int ticks) {
		return new AnimationController<T>(animatable, "Spawn", 0, state -> {
			if (animatable.getTick(objectSupplier.apply(state)) <= ticks)
				return state.setAndContinue(DefaultAnimations.SPAWN);

			return PlayState.STOP;
		});
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericWalkController(T animatable) {
		return new AnimationController<T>(animatable, "Walk", 0, state -> {
			if (state.isMoving())
				return state.setAndContinue(WALK);

			return PlayState.STOP;
		});
	}

	public static <T extends LivingEntity & BlueAnimatable> AnimationController<T> genericAttackAnimation(T animatable, RawAnimation attackAnimation) {
		return new AnimationController<>(animatable, "Attack", 5, state -> {
			if (animatable.swinging)
				return state.setAndContinue(attackAnimation);

			state.getController().forceAnimationReset();

			return PlayState.STOP;
		});
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericWalkIdleController(T animatable) {
		return new AnimationController<T>(animatable, "Walk/Idle", 0, state -> state.setAndContinue(state.isMoving() ? WALK : IDLE));
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericSwimController(T entity) {
		return new AnimationController<T>(entity, "Swim", 0, state -> {
			if (state.isMoving())
				return state.setAndContinue(SWIM);

			return PlayState.STOP;
		});
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericSwimIdleController(T animatable) {
		return new AnimationController<T>(animatable, "Swim/Idle", 0, state -> state.setAndContinue(state.isMoving() ? SWIM : IDLE));
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericFlyController(T animatable) {
		return new AnimationController<T>(animatable, "Fly", 0, state -> state.setAndContinue(FLY));
	}

	public static <T extends BlueAnimatable> AnimationController<T> genericFlyIdleController(T animatable) {
		return new AnimationController<T>(animatable, "Fly/Idle", 0, state -> state.setAndContinue(state.isMoving() ? FLY : IDLE));
	}

	public static <T extends Entity & BlueAnimatable> AnimationController<T> genericWalkRunIdleController(T entity) {
		return new AnimationController<T>(entity, "Walk/Run/Idle", 0, state -> {
			if (state.isMoving()) {
				return state.setAndContinue(entity.isSprinting() ? RUN : WALK);
			} else {
				return state.setAndContinue(IDLE);
			}
		});
	}
}
