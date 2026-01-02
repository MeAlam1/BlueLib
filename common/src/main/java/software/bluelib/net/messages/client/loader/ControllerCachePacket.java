/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net.messages.client.loader;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;
import software.bluelib.loader.cache.controller.ControllerCache;

public record ControllerCachePacket(
		@NotNull Map<ResourceLocation, ControllerCache> allControllers) implements NetworkPacket<ControllerCachePacket> {

	@NotNull
	public static final ResourceLocation ID = BlueResource.resource("controller_cache");

	private static final String KEY_CONTROLLERS = "controllers";

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		CompoundTag root = new CompoundTag();
		CompoundTag controllersTag = new CompoundTag();

		for (Map.Entry<ResourceLocation, ControllerCache> entry : allControllers.entrySet()) {
			ResourceLocation id = entry.getKey();
			ControllerCache cache = entry.getValue();
			if (id == null || cache == null) continue;

			CompoundTag cacheTag = new CompoundTag();
			cache.writeToNBT(cacheTag);
			controllersTag.put(id.toString(), cacheTag);
		}

		root.put(KEY_CONTROLLERS, controllersTag);
		pBuffer.writeNbt(root);
	}

	public static @NotNull ControllerCachePacket decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		CompoundTag root = pBuffer.readNbt();
		if (root == null) root = new CompoundTag();

		CompoundTag controllersTag = root.getCompound(KEY_CONTROLLERS);
		Map<ResourceLocation, ControllerCache> controllers = new HashMap<>();

		for (String key : controllersTag.getAllKeys()) {
			ResourceLocation id = ResourceLocation.tryParse(key);
			if (id == null) continue;

			CompoundTag cacheTag = controllersTag.getCompound(key);
			ControllerCache cache = ControllerCache.readFromNBT(cacheTag);
			controllers.put(id, cache);
		}

		return new ControllerCachePacket(controllers);
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}
}
