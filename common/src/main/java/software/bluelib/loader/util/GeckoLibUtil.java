/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.loader.json.model.ModelCacheFactory;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.loader.animatable.instance.InstancedAnimatableInstanceCache;
import software.bluelib.loader.animatable.instance.SingletonAnimatableInstanceCache;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.animation.EasingType;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;

@SuppressWarnings("unused")
public final class GeckoLibUtil {

    private static final Int2ObjectMap<String> ANIMATABLE_IDENTITIES = new Int2ObjectOpenHashMap<>();
    public static final Map<String, GeoAnimatable> SYNCED_ANIMATABLES = new Object2ObjectOpenHashMap<>();

    public static AnimatableInstanceCache createInstanceCache(GeoAnimatable pAnimatable) {
        AnimatableInstanceCache cache = pAnimatable.animatableCacheOverride();

        return cache != null ? cache : createInstanceCache(pAnimatable, !(pAnimatable instanceof Entity) && !(pAnimatable instanceof BlockEntity));
    }

    public static AnimatableInstanceCache createInstanceCache(GeoAnimatable pAnimatable, boolean pSingletonObject) {
        AnimatableInstanceCache cache = pAnimatable.animatableCacheOverride();

        if (cache != null)
            return cache;

        return pSingletonObject ? new SingletonAnimatableInstanceCache(pAnimatable) : new InstancedAnimatableInstanceCache(pAnimatable);
    }

    synchronized public static Animation.LoopType addCustomLoopType(String pName, Animation.LoopType pLoopType) {
        return Animation.LoopType.register(pName, pLoopType);
    }

    synchronized public static EasingType addCustomEasingType(String pName, EasingType pEasingType) {
        return EasingType.register(pName, pEasingType);
    }

    synchronized public static void addCustomBakedModelFactory(String pNamespace, ModelCacheFactory pFactory) {
        ModelCacheFactory.register(pNamespace, pFactory);
    }

    synchronized public static <D> SerializableDataTicket<D> addDataTicket(SerializableDataTicket<D> pDataTicket) {
        return DataTickets.registerSerializable(pDataTicket);
    }

    synchronized public static void registerSyncedAnimatable(GeoAnimatable pAnimatable) {
        GeoAnimatable existing = SYNCED_ANIMATABLES.put(getSyncedSingletonAnimatableId(pAnimatable), pAnimatable);

        //if (existing == null)
        ////GeckoLibConstants.LOGGER.debug("Registered SyncedAnimatable for " + animatable.getClass());
    }

    @Nullable
    public static GeoAnimatable getSyncedAnimatable(String pSyncedAnimatableId) {
        GeoAnimatable animatable = SYNCED_ANIMATABLES.get(pSyncedAnimatableId);

        //if (animatable == null)
        ////GeckoLibConstants.LOGGER.error("Attempting to retrieve unregistered synced animatable! (" + syncedAnimatableId + ")");

        return animatable;
    }

    public static String getSyncedSingletonAnimatableId(GeoAnimatable pAnimatable) {
        return ANIMATABLE_IDENTITIES.computeIfAbsent(System.identityHashCode(pAnimatable), i -> {
            String baseId = pAnimatable.getClass().getName();
            i = 0;

            while (SYNCED_ANIMATABLES.containsKey(baseId + i)) {
                i++;
            }

            return baseId + i;
        });
    }
}
