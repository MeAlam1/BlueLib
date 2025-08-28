/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class LeashableMoLang extends BaseMoLangContext {

	public LeashableMoLang(@NotNull Supplier<Leashable> pLeashableSup) {
		Leashable leashableEntity = pLeashableSup.get();
		setVariable("leash_tag", Leashable.LEASH_TAG);
		setVariable("leash_too_far_dist", Leashable.LEASH_TOO_FAR_DIST);
		setVariable("leash_elastic_dist", Leashable.LEASH_ELASTIC_DIST);
		setVariable("get_leash_data", leashableEntity.getLeashData());
		setVariable("is_leashed", leashableEntity.isLeashed());
		setVariable("can_have_a_leash_attached_to_it", leashableEntity.canHaveALeashAttachedToIt());
		setVariable("can_be_leashed", leashableEntity.canBeLeashed());
		setVariable("get_leash_holder", leashableEntity.getLeashHolder());

		registerFunction("set_leash_data", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Leashable.LeashData data)) {
				return leashableEntity.getLeashData();
			}
			leashableEntity.setLeashData(data);
			return leashableEntity.getLeashData();
		});

		registerFunction("set_delayed_leash_holder_id", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Integer data)) {
				return false;
			}
			leashableEntity.setDelayedLeashHolderId(data);
			return true;
		});

		registerFunction("read_leash_data", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof CompoundTag data)) {
				return false;
			}
			return leashableEntity.readLeashData(data);
		});

		registerFunction("write_leash_data", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof CompoundTag) && !(args.get(1) instanceof Leashable.LeashData))) {
				return false;
			}
			CompoundTag data = (CompoundTag) args.getFirst();
			Leashable.LeashData leashData = (Leashable.LeashData) args.get(1);
			leashableEntity.writeLeashData(data, leashData);
			return leashableEntity.readLeashData(data);
		});

		registerFunction("drop_leash", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof Boolean) && !(args.get(1) instanceof Boolean))) {
				return false;
			}
			boolean sendPacket = (Boolean) args.getFirst();
			boolean dropItem = (Boolean) args.get(1);
			leashableEntity.dropLeash(sendPacket, dropItem);
			return true;
		});

		registerFunction("handle_leash_at_distance", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof Entity) && !(args.get(1) instanceof Number))) {
				return false;
			}
			Entity entity = (Entity) args.getFirst();
			float distance = ((Number) args.get(1)).floatValue();
			leashableEntity.handleLeashAtDistance(entity, distance);
			return true;
		});

		registerFunction("leash_too_far_behaviour", (args, runtime) -> {
			leashableEntity.leashTooFarBehaviour();
			return true;
		});

		registerFunction("close_range_leash_behaviour", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Entity entity)) {
				return false;
			}
			leashableEntity.closeRangeLeashBehaviour(entity);
			return true;
		});

		registerFunction("elastic_range_leash_behaviour", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof Entity) && !(args.get(1) instanceof Number))) {
				return false;
			}
			Entity entity = (Entity) args.getFirst();
			float distance = ((Number) args.get(1)).floatValue();
			leashableEntity.elasticRangeLeashBehaviour(entity, distance);
			return true;
		});

		registerFunction("set_leash_to", (args, runtime) -> {
			if (args.size() != 2 || (!(args.getFirst() instanceof Entity) && !(args.get(1) instanceof Boolean))) {
				return false;
			}
			Entity entity = (Entity) args.getFirst();
			boolean broadcast = (Boolean) args.get(1);
			leashableEntity.setLeashedTo(entity, broadcast);
			return true;
		});
	}
}
