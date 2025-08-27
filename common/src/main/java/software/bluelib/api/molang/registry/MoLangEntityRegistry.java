/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.registry;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import software.bluelib.api.molang.context.entity.*;
import software.bluelib.api.molang.context.entity.animal.AbstractHorseMoLang;
import software.bluelib.api.molang.context.entity.animal.AnimalMoLang;
import software.bluelib.api.molang.context.entity.animal.BeeMoLang;
import software.bluelib.api.molang.context.entity.animal.TamableAnimalMoLang;
import software.bluelib.api.molang.context.entity.decoration.ArmorStandMoLang;
import software.bluelib.api.molang.context.entity.decoration.BlockAttachedEntityMoLang;
import software.bluelib.api.molang.context.entity.decoration.HangingEntityMoLang;
import software.bluelib.api.molang.context.entity.mob.AgeableMobMoLang;
import software.bluelib.api.molang.context.entity.mob.PathfinderMobMoLang;
import software.bluelib.api.molang.context.entity.monster.AbstractIllagerMoLang;
import software.bluelib.api.molang.context.entity.monster.MonsterMoLang;
import software.bluelib.api.molang.context.entity.monster.PatrollingMonsterMoLang;
import software.bluelib.api.molang.context.entity.npc.AbstractVillagerMoLang;

public class MoLangEntityRegistry extends MoLangContextRegistry {

	public static void init() {
		registerEntityContext(wolf -> wolf instanceof Wolf ? new WolfMoLang(() -> (Wolf) wolf) : null);
		registerEntityContext(bee -> bee instanceof Bee ? new BeeMoLang(() -> (Bee) bee) : null);
		registerEntityContext(villager -> villager instanceof Villager ? new VillagerMoLang(() -> (Villager) villager) : null);
		registerEntityContext(abstractVillager -> abstractVillager instanceof AbstractVillager ? new AbstractVillagerMoLang(() -> (AbstractVillager) abstractVillager) : null);
		registerEntityContext(abstractIllager -> abstractIllager instanceof AbstractIllager ? new AbstractIllagerMoLang(() -> (AbstractIllager) abstractIllager) : null);
		registerEntityContext(abstractHorse -> abstractHorse instanceof AbstractHorse ? new AbstractHorseMoLang(() -> (AbstractHorse) abstractHorse) : null);
		registerEntityContext(tamableAnimal -> tamableAnimal instanceof TamableAnimal ? new TamableAnimalMoLang(() -> (TamableAnimal) tamableAnimal) : null);
		registerEntityContext(animal -> animal instanceof Animal ? new AnimalMoLang(() -> (Animal) animal) : null);
		registerEntityContext(ageableMob -> ageableMob instanceof AgeableMob ? new AgeableMobMoLang(() -> (AgeableMob) ageableMob) : null);
		registerEntityContext(raider -> raider instanceof Raider ? new RaiderMoLang(() -> (Raider) raider) : null);
		registerEntityContext(patrollingMonster -> patrollingMonster instanceof PatrollingMonster ? new PatrollingMonsterMoLang(() -> (PatrollingMonster) patrollingMonster) : null);
		registerEntityContext(monster -> monster instanceof Monster ? new MonsterMoLang(() -> (Monster) monster) : null);
		registerEntityContext(pathfinderMob -> pathfinderMob instanceof PathfinderMob ? new PathfinderMobMoLang(() -> (PathfinderMob) pathfinderMob) : null);
		registerEntityContext(mob -> mob instanceof Mob ? new MobMoLang(() -> (Mob) mob) : null);
		registerEntityContext(armorStand -> armorStand instanceof ArmorStand ? new ArmorStandMoLang(() -> (ArmorStand) armorStand) : null);
		registerEntityContext(player -> player instanceof Player ? new PlayerMoLang(() -> (Player) player) : null);
		registerEntityContext(livingEntity -> livingEntity instanceof LivingEntity ? new LivingEntityMoLang(() -> (LivingEntity) livingEntity) : null);
		registerEntityContext(hangingEntity -> hangingEntity instanceof HangingEntity ? new HangingEntityMoLang(() -> (HangingEntity) hangingEntity) : null);
		registerEntityContext(blockAttachedEntity -> blockAttachedEntity instanceof BlockAttachedEntity ? new BlockAttachedEntityMoLang(() -> (BlockAttachedEntity) blockAttachedEntity) : null);
		registerEntityContext(vehicleEntity -> vehicleEntity instanceof VehicleEntity ? new VehicleEntityMoLang(() -> (VehicleEntity) vehicleEntity) : null);

		registerEntityContext(saddleableEntity -> saddleableEntity instanceof Saddleable ? new SaddleableMoLang(() -> (Saddleable) saddleableEntity) : null);
		registerEntityContext(ownableEntity -> ownableEntity instanceof OwnableEntity ? new OwnableMoLang(() -> (OwnableEntity) ownableEntity) : null);

		registerEntityContext(entity -> entity instanceof Entity ? new EntityMoLang(() -> entity) : null);
	}
}
