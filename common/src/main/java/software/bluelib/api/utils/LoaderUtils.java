/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.animations.AnimationCache;
import software.bluelib.oldLoader.animation.EasingType;
import software.bluelib.oldLoader.constant.DataTickets;
import software.bluelib.oldLoader.constant.dataticket.SerializableDataTicket;

@SuppressWarnings("unused")
public final class LoaderUtils {

	@NotNull
	private static final Int2ObjectMap<String> ANIMATABLE_IDENTITIES = new Int2ObjectOpenHashMap<>();
	@NotNull
	public static final Map<String, BlueAnimatable> SYNCED_ANIMATABLES = new Object2ObjectOpenHashMap<>();

	public static <F> void addCustomFactory(@NotNull String pNamespace, @NotNull F pFactory, @NotNull BiConsumer<String, F> pRegisterFunction) {
		synchronized (LoaderUtils.class) {
			pRegisterFunction.accept(pNamespace, pFactory);
		}
	}

	synchronized public static @NotNull AnimationCache.LoopType addCustomLoopType(@NotNull String pName, @NotNull AnimationCache.LoopType pLoopType) {
		return AnimationCache.LoopType.register(pName, pLoopType);
	}

	synchronized public static @NotNull EasingType addCustomEasingType(@NotNull String pName, @NotNull EasingType pEasingType) {
		return EasingType.register(pName, pEasingType);
	}

	synchronized public static <D> @NotNull SerializableDataTicket<D> addDataTicket(@NotNull SerializableDataTicket<D> pDataTicket) {
		return DataTickets.registerSerializable(pDataTicket);
	}

	synchronized public static void registerSyncedAnimatable(@NotNull BlueAnimatable pAnimatable) {
		BlueAnimatable existing = SYNCED_ANIMATABLES.put(getSyncedSingletonAnimatableId(pAnimatable), pAnimatable);
	}

	@Nullable
	public static BlueAnimatable getSyncedAnimatable(@NotNull String pSyncedAnimatableId) {
		return SYNCED_ANIMATABLES.get(pSyncedAnimatableId);
	}

	public static @NotNull String getSyncedSingletonAnimatableId(@NotNull BlueAnimatable pAnimatable) {
		return ANIMATABLE_IDENTITIES.computeIfAbsent(System.identityHashCode(pAnimatable), i -> {
			String baseId = pAnimatable.getClass().getName();
			i = 0;

			while (SYNCED_ANIMATABLES.containsKey(baseId + i)) {
				i++;
			}

			return baseId + i;
		});
	}

	public static ResourceLocation stripSuffix(String pSuffix, ResourceLocation pLocation) {
		String path = pLocation.getPath();
		if (path.endsWith(pSuffix)) {
			String newPath = path.substring(0, path.length() - pSuffix.length());
			return pLocation.withPath(newPath);
		} else {
			throw new RuntimeException("Invalid file type: expected a " + pSuffix + " file, got: " + path);
		}
	}
}
