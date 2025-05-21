package software.bluelib_examples;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import software.bluelib_examples.platform.IPlatform;

import java.util.function.Supplier;

public class BlueLibPlatform implements IPlatform {

	@Override
	public <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entity) {
		return BlueLib.ENTITIES.register(id, entity);
	}
}
