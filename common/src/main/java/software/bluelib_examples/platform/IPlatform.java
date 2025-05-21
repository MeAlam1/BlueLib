package software.bluelib_examples.platform;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface IPlatform {
	<T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entity);
}
