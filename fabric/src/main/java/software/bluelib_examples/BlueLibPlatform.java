package software.bluelib_examples;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import software.bluelib_examples.platform.IPlatform;

import java.util.function.Supplier;

public class BlueLibPlatform implements IPlatform {
	@Override
	public <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entity) {
		return registerSupplier(BuiltInRegistries.ENTITY_TYPE, id, entity);
	}

	/**
	 * Quick wrapper to make the individual registration lines cleaner but still return the multiloader-compatible supplier
	 */
	private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R registry, String id, Supplier<T> object) {
		final T registeredObject = Registry.register((Registry<T>) registry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, id), object.get());

		return () -> registeredObject;
	}

	/**
	 * Quick wrapper to make the individual registration lines cleaner but still return the multiloader-compatible supplier
	 */
	private static <T, R extends Registry<? super T>> Holder<T> registerHolder(R registry, String id, Supplier<T> object) {
		return Registry.registerForHolder((Registry<T>) registry, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, id), object.get());
	}
}
