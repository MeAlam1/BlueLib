package software.bluelib.loader.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.loader.animatable.instance.InstancedAnimatableInstanceCache;
import software.bluelib.loader.animatable.instance.SingletonAnimatableInstanceCache;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.animation.EasingType;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;
import software.bluelib.loader.loading.object.BakedModelFactory;

import java.util.Map;


public final class GeckoLibUtil {
	private static final Int2ObjectMap<String> ANIMATABLE_IDENTITIES = new Int2ObjectOpenHashMap<>();
	public static final Map<String, GeoAnimatable> SYNCED_ANIMATABLES = new Object2ObjectOpenHashMap<>();

	
	public static AnimatableInstanceCache createInstanceCache(GeoAnimatable animatable) {
		AnimatableInstanceCache cache = animatable.animatableCacheOverride();

		return cache != null ? cache : createInstanceCache(animatable, !(animatable instanceof Entity) && !(animatable instanceof BlockEntity));
	}

	
	public static AnimatableInstanceCache createInstanceCache(GeoAnimatable animatable, boolean singletonObject) {
		AnimatableInstanceCache cache = animatable.animatableCacheOverride();

		if (cache != null)
			return cache;

		return singletonObject ? new SingletonAnimatableInstanceCache(animatable) : new InstancedAnimatableInstanceCache(animatable);
	}

	
	synchronized public static Animation.LoopType addCustomLoopType(String name, Animation.LoopType loopType) {
		return Animation.LoopType.register(name, loopType);
	}

	
	synchronized public static EasingType addCustomEasingType(String name, EasingType easingType) {
		return EasingType.register(name, easingType);
	}

	
	synchronized public static void addCustomBakedModelFactory(String namespace, BakedModelFactory factory) {
		BakedModelFactory.register(namespace, factory);
	}

	
	synchronized public static <D> SerializableDataTicket<D> addDataTicket(SerializableDataTicket<D> dataTicket) {
		return DataTickets.registerSerializable(dataTicket);
	}

	
	synchronized public static void registerSyncedAnimatable(GeoAnimatable animatable) {
		GeoAnimatable existing = SYNCED_ANIMATABLES.put(getSyncedSingletonAnimatableId(animatable), animatable);

		//if (existing == null)
			//GeckoLibConstants.LOGGER.debug("Registered SyncedAnimatable for " + animatable.getClass());
	}

	
	@Nullable
	public static GeoAnimatable getSyncedAnimatable(String syncedAnimatableId) {
		GeoAnimatable animatable = SYNCED_ANIMATABLES.get(syncedAnimatableId);

		//if (animatable == null)
			//GeckoLibConstants.LOGGER.error("Attempting to retrieve unregistered synced animatable! (" + syncedAnimatableId + ")");

		return animatable;
	}

	
	public static String getSyncedSingletonAnimatableId(GeoAnimatable animatable) {
		return ANIMATABLE_IDENTITIES.computeIfAbsent(System.identityHashCode(animatable), i -> {
			String baseId = animatable.getClass().getName();
			i = 0;

			while (SYNCED_ANIMATABLES.containsKey(baseId + i)) {
				i++;
			}

			return baseId + i;
		});
	}
}
