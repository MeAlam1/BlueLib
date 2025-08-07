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

public class MoLangEntityRegistry extends MoLangContextRegistry {

	public static void init() {
		MoLangEntityRegistry.registerEntityContext(wolf -> {
			if (wolf instanceof Wolf pWolf) {
				return new WolfMoLang(() -> pWolf);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(bee -> {
			if (bee instanceof Bee pBee) {
				return new BeeMoLang(() -> pBee);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(villager -> {
			if (villager instanceof Villager pVillager) {
				return new VillagerMoLang(() -> pVillager);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(abstractVillager -> {
			if (abstractVillager instanceof AbstractVillager pVillager) {
				return new AbstractVillagerMoLang(() -> pVillager);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(abstractIllager -> {
			if (abstractIllager instanceof AbstractIllager pAbstractIllager) {
				return new AbstractIllagerMoLang(() -> pAbstractIllager);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(abstractHorse -> {
			if (abstractHorse instanceof AbstractHorse pAbstractHorse) {
				return new AbstractHorseMoLang(() -> pAbstractHorse);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(tamableAnimal -> {
			if (tamableAnimal instanceof TamableAnimal pTamableAnimal) {
				return new TamableAnimalMoLang(() -> pTamableAnimal);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(animal -> {
			if (animal instanceof Animal pAnimal) {
				return new AnimalMoLang(() -> pAnimal);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(ageableMob -> {
			if (ageableMob instanceof AgeableMob pAgeableMob) {
				return new AgeableMobMoLang(() -> pAgeableMob);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(raider -> {
			if (raider instanceof Raider pRaider) {
				return new RaiderMoLang(() -> pRaider);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(patrollingMonster -> {
			if (patrollingMonster instanceof PatrollingMonster pPatrollingMonster) {
				return new PatrollingMonsterMoLang(() -> pPatrollingMonster);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(monster -> {
			if (monster instanceof Monster pMonster) {
				return new MonsterMoLang(() -> pMonster);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(pathfinderMob -> {
			if (pathfinderMob instanceof PathfinderMob pPathfinderMob) {
				return new PathfinderMobMoLang(() -> pPathfinderMob);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(mob -> {
			if (mob instanceof Mob pMob) {
				return new MobMoLang(() -> pMob);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(armorStand -> {
			if (armorStand instanceof ArmorStand pArmorStand) {
				return new ArmorStandMoLang(() -> pArmorStand);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(player -> {
			if (player instanceof Player pPlayer) {
				return new PlayerMoLang(() -> pPlayer);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(livingEntity -> {
			if (livingEntity instanceof LivingEntity pLivingEntity) {
				return new LivingEntityMoLang(() -> pLivingEntity);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(hangingEntity -> {
			if (hangingEntity instanceof HangingEntity pHangingEntity) {
				return new HangingEntityMoLang(() -> pHangingEntity);
			}
			return null;
		});
		MoLangEntityRegistry.registerEntityContext(blockAttachedEntity -> {
			if (blockAttachedEntity instanceof BlockAttachedEntity pBlockAttachedEntity) {
				return new BlockAttachedEntityMoLang(() -> pBlockAttachedEntity);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(vehicleEntity -> {
			if (vehicleEntity instanceof VehicleEntity pVehicleEntity) {
				return new VehicleEntityMoLang(() -> pVehicleEntity);
			}
			return null;
		});

		MoLangEntityRegistry.registerEntityContext(entity -> {
			if (entity instanceof Entity pEntity) {
				return new EntityMoLang(() -> pEntity);
			}
			return null;
		});
	}
}
