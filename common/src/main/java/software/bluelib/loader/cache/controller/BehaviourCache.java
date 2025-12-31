/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.controller;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import java.util.List;
import java.util.Map;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.codec.NbtCodecUtils;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.api.utils.minecraft.CompoundTagUtils;

public record BehaviourCache(
		@NotNull List<String> conditions,
		@Nullable Integer priority,
		@NotNull Map<String, StateCache> states) {

	@NotNull
	public static final Codec<BehaviourCache> CODEC =
			NbtCodecUtils.fromNbt(BehaviourCache::readFromNBT, BehaviourCache::writeToNBT);

	@NotNull
	public static final DataComponentType<BehaviourCache> BEHAVIOUR_CACHE_DATA =
			NbtCodecUtils.persistentDataType(CODEC);

	public void writeToNBT(@NotNull CompoundTag pTag) {
		CompoundTagUtils.writeList(
				pTag,
				"conditions",
				conditions,
				(tag, data) -> tag.putString("condition", data));
		if (priority != null) {
			pTag.putInt("priority", priority);
		}
		CompoundTagUtils.writeMap(
				pTag,
				"states",
				states,
				(tag, key) -> tag.putString("name", key),
				(tag, value) -> value.writeToNBT(tag),
				"name",
				"state");
	}

	@NotNull
	public static BehaviourCache readFromNBT(@NotNull CompoundTag pTag) {
		List<String> conditions = CompoundTagUtils.readList(
				pTag,
				"conditions",
				tag -> tag.getString("condition"));
		Integer priority = pTag.contains("priority") ? pTag.getInt("priority") : null;
		Map<String, StateCache> states = CompoundTagUtils.readMap(
				pTag,
				"states",
				tag -> tag.getString("name"),
				StateCache::readFromNBT,
				"name",
				"state");
		return new BehaviourCache(conditions, priority, states);
	}

	@Nullable
	public StateCache getState(@NotNull String pName) {
		StateCache state = states.get(pName);
		if (state == null) {
			BaseLogger.log(BaseLogLevel.WARNING, "State not found: " + pName);
		}
		return state;
	}

	@NotNull
	public StateCache getMainState() {
		return states.values().iterator().next();
	}
}
