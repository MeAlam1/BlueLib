/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.animal;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AnimalMoLang extends BaseMoLangContext {

	public AnimalMoLang(@NotNull Supplier<Animal> pAnimalSup) {
		Animal animal = pAnimalSup.get();
		setVariable("is_in_love", animal.isInLove());
		setVariable("can_fall_in_love", animal.canFallInLove());
		setVariable("get_ambient_sound_interval", animal.getAmbientSoundInterval());
		setVariable("get_love_cause", animal.getLoveCause());

		registerFunction("check_animal_spawn_rules", (args, runtime) -> {
			if (args.size() != 5 || (!(args.getFirst() instanceof EntityType) &&
					!(args.get(1) instanceof LevelAccessor) &&
					!(args.get(2) instanceof MobSpawnType) &&
					!(args.get(3) instanceof BlockPos) &&
					!(args.get(4) instanceof RandomSource))) {
				return false;
			}
			EntityType<?> entityType = (EntityType<?>) args.getFirst();
			LevelAccessor levelAccessor = (LevelAccessor) args.get(1);
			MobSpawnType mobSpawnType = (MobSpawnType) args.get(2);
			BlockPos blockPos = (BlockPos) args.get(3);
			RandomSource randomSource = (RandomSource) args.get(4);
			return Animal.checkAnimalSpawnRules((EntityType<? extends Animal>) entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
		});

		registerFunction("remove_when_far_away", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number distance)) {
				return false;
			}
			double distanceToClosestPlayer = distance.doubleValue();
			return animal.removeWhenFarAway(distanceToClosestPlayer);
		});

		registerFunction("is_food", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof ItemStack item)) {
				return false;
			}
			return animal.isFood(item);
		});

		registerFunction("set_in_love", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Player player)) {
				return animal.isInLove();
			}
			animal.setInLove(player);
			return animal.isInLove();
		});

		registerFunction("set_in_love_time", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number time)) {
				return animal.getInLoveTime();
			}
			int inLoveTime = time.intValue();
			animal.setInLoveTime(inLoveTime);
			return animal.getInLoveTime();
		});

		registerFunction("reset_love", (args, runtime) -> {
			animal.resetLove();
			return true;
		});

		registerFunction("can_mate", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Animal mate)) {
				return false;
			}
			return animal.canMate(mate);
		});

		registerFunction("spawn_child_from_breeding", (args, runtime) -> {
			if (args.size() != 1 || (!(args.getFirst() instanceof ServerLevel) && !(args.get(1) instanceof Animal))) {
				return false;
			}
			ServerLevel serverLevel = (ServerLevel) args.getFirst();
			Animal mate = (Animal) args.get(1);
			animal.spawnChildFromBreeding(serverLevel, mate);
			return true;
		});

		registerFunction("finalize_spawn_child_from_breeding", (args, runtime) -> {
			if (args.size() != 1 || (!(args.getFirst() instanceof ServerLevel) && !(args.get(1) instanceof Animal) && !(args.get(2) instanceof AgeableMob))) {
				return false;
			}
			ServerLevel serverLevel = (ServerLevel) args.getFirst();
			Animal mate = (Animal) args.get(1);
			AgeableMob child = (AgeableMob) args.get(2);
			animal.finalizeSpawnChildFromBreeding(serverLevel, mate, child);
			return true;
		});
	}
}
