/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.animal;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AbstractHorseMoLang extends BaseMoLangContext {

	public AbstractHorseMoLang(@NotNull Supplier<AbstractHorse> pAbstractHorseSup) {
		AbstractHorse abstractHorse = pAbstractHorseSup.get();
		setVariable("is_tamed", abstractHorse.isTamed());
		setVariable("is_jumping", abstractHorse.isJumping());
		setVariable("is_eating", abstractHorse.isEating());
		setVariable("is_standing", abstractHorse.isStanding());
		setVariable("is_bred", abstractHorse.isBred());
		setVariable("get_temper", abstractHorse.getTemper());
		setVariable("tail_counter", abstractHorse.tailCounter);
		setVariable("sprint_counter", abstractHorse.sprintCounter);
		setVariable("get_max_temper", abstractHorse.getMaxTemper());
		setVariable("can_eat_grass", abstractHorse.canEatGrass());
		setVariable("get_inventory_columns", abstractHorse.getInventoryColumns());
		setVariable("get_inventory_size", abstractHorse.getInventorySize());
		setVariable("get_body_armor_access", abstractHorse.getBodyArmorAccess());
		setVariable("get_ambient_stand_interval", abstractHorse.getAmbientStandInterval());
		setVariable("get_ambient_stand_sound", abstractHorse.getAmbientStandSound());

		setVariable("inv_slot_saddle", AbstractHorse.INV_SLOT_SADDLE);
		setVariable("inv_base_count", AbstractHorse.INV_BASE_COUNT);
		setVariable("breeding_cross_factor", AbstractHorse.BREEDING_CROSS_FACTOR);
		setVariable("inventory_slot_offset", AbstractHorse.INVENTORY_SLOT_OFFSET);
		setVariable("chest_slot_offset", AbstractHorse.CHEST_SLOT_OFFSET);
		setVariable("equipment_slot_offset", AbstractHorse.EQUIPMENT_SLOT_OFFSET);

		registerFunction("get_eat_anim", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number partial)) {
				return 0.0;
			}
			float partialTick = partial.floatValue();
			return abstractHorse.getEatAnim(partialTick);
		});

		registerFunction("get_stand_anim", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number partial)) {
				return 0.0;
			}
			float partialTick = partial.floatValue();
			return abstractHorse.getEatAnim(partialTick);
		});

		registerFunction("get_mouth_anim", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number partial)) {
				return 0.0;
			}
			float partialTick = partial.floatValue();
			return abstractHorse.getEatAnim(partialTick);
		});

		registerFunction("tame_with_name", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Player player)) {
				return abstractHorse.isTamed();
			}
			return abstractHorse.tameWithName(player);
		});

		registerFunction("fed_food", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof Player) && !(args.get(1) instanceof ItemStack))) {
				return InteractionResult.FAIL;
			}
			Player player = (Player) args.getFirst();
			ItemStack itemStack = (ItemStack) args.get(1);
			return abstractHorse.fedFood(player, itemStack);
		});

		registerFunction("equip_body_armor", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof Player) && !(args.get(1) instanceof ItemStack))) {
				return abstractHorse.getBodyArmorItem();
			}
			Player player = (Player) args.getFirst();
			ItemStack itemStack = (ItemStack) args.get(1);
			abstractHorse.equipBodyArmor(player, itemStack);
			return abstractHorse.getBodyArmorItem();
		});

		registerFunction("make_mad", (args, runtime) -> {
			abstractHorse.makeMad();
			return true;
		});

		registerFunction("stand_if_possible", (args, runtime) -> {
			abstractHorse.standIfPossible();
			return abstractHorse.isStanding();
		});

		registerFunction("set_standing", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean standing)) {
				return abstractHorse.isStanding();
			}
			abstractHorse.setStanding(standing);
			return abstractHorse.isStanding();
		});

		registerFunction("set_eating", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean eating)) {
				return abstractHorse.isEating();
			}
			abstractHorse.setEating(eating);
			return abstractHorse.isEating();
		});

		registerFunction("modify_temper", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number temperToAdd)) {
				return abstractHorse.getTemper();
			}
			int addTemper = temperToAdd.intValue();
			return abstractHorse.modifyTemper(addTemper);
		});

		registerFunction("set_temper", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number temperToAdd)) {
				return abstractHorse.getTemper();
			}
			int addTemper = temperToAdd.intValue();
			abstractHorse.setTemper(addTemper);
			return abstractHorse.getTemper();
		});

		registerFunction("set_bred", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean breeding)) {
				return abstractHorse.isBred();
			}
			abstractHorse.setBred(breeding);
			return abstractHorse.isBred();
		});

		registerFunction("set_is_jumping", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean jumping)) {
				return abstractHorse.isJumping();
			}
			abstractHorse.setIsJumping(jumping);
			return abstractHorse.isJumping();
		});

		registerFunction("set_tamed", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean tamed)) {
				return abstractHorse.isTamed();
			}
			abstractHorse.setTamed(tamed);
			return abstractHorse.isTamed();
		});

		registerFunction("set_owner_uuid", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof UUID uuid)) {
				return abstractHorse.isTamed();
			}
			abstractHorse.setOwnerUUID(uuid);
			return abstractHorse.isTamed();
		});
	}
}
