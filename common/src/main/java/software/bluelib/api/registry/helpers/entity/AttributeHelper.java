package software.bluelib.api.registry.helpers.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class AttributeHelper {

	private static final List<Entry<?>> ENTRIES = new ArrayList<>();

	public static <T extends LivingEntity> void queueAttributes(Supplier<EntityType<T>> type, Supplier<AttributeSupplier.Builder> attributes) {
		ENTRIES.add(new Entry<>(type, attributes));
	}

	public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> registrar) {
		for (Entry<?> entry : ENTRIES) {
			registrar.accept(entry.type.get(), entry.attributes.get().build());
		}
	}

	private record Entry<T extends LivingEntity>(Supplier<EntityType<T>> type,
			Supplier<AttributeSupplier.Builder> attributes) {}
}
