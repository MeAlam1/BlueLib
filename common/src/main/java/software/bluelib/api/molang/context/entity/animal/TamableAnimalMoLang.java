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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class TamableAnimalMoLang extends BaseMoLangContext {

	public TamableAnimalMoLang(@NotNull Supplier<TamableAnimal> pTamableAnimalSup) {
		TamableAnimal tamableAnimal = pTamableAnimalSup.get();
		setVariable("teleport_when_distance_is_sq", TamableAnimal.TELEPORT_WHEN_DISTANCE_IS_SQ);
		setVariable("is_tame", tamableAnimal.isTame());
		setVariable("is_in_sitting_pose", tamableAnimal.isInSittingPose());
		setVariable("is_ordered_to_sit", tamableAnimal.isOrderedToSit());
		setVariable("should_try_teleport_to_owner", tamableAnimal.shouldTryTeleportToOwner());
		setVariable("unable_to_move_to_owner", tamableAnimal.unableToMoveToOwner());

		registerFunction("set_tame", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof Boolean) && !(args.get(1) instanceof Boolean))) {
				return tamableAnimal.isTame();
			}
			boolean tame = (Boolean) args.getFirst();
			boolean applyTamingSideEffects = (Boolean) args.get(1);
			tamableAnimal.setTame(tame, applyTamingSideEffects);
			return tamableAnimal.isTame();
		});

		registerFunction("set_in_sitting_pose", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean sitting)) {
				return tamableAnimal.isInSittingPose();
			}
			tamableAnimal.setInSittingPose(sitting);
			return tamableAnimal.isInSittingPose();
		});

		registerFunction("set_owner_uuid", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof UUID owner)) {
				return tamableAnimal.getOwnerUUID();
			}
			tamableAnimal.setOwnerUUID(owner);
			return tamableAnimal.getOwnerUUID();
		});

		registerFunction("tame", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Player owner)) {
				return tamableAnimal.isTame();
			}
			tamableAnimal.tame(owner);
			return tamableAnimal.isTame();
		});

		registerFunction("is_owned_by", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof LivingEntity owner)) {
				return false;
			}
			return tamableAnimal.isOwnedBy(owner);
		});

		registerFunction("wants_to_attack", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof LivingEntity) && !(args.get(1) instanceof LivingEntity))) {
				return false;
			}
			LivingEntity target = (LivingEntity) args.getFirst();
			LivingEntity owner = (LivingEntity) args.get(1);
			return tamableAnimal.wantsToAttack(target, owner);
		});

		registerFunction("set_ordered_to_sit", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean order)) {
				return tamableAnimal.isOrderedToSit();
			}
			tamableAnimal.setOrderedToSit(order);
			return tamableAnimal.isOrderedToSit();
		});

		registerFunction("try_to_teleport_to_owner", (args, runtime) -> {
			tamableAnimal.tryToTeleportToOwner();
			return true;
		});
	}
}
