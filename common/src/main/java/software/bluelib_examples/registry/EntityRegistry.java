package software.bluelib_examples.registry;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import software.bluelib_examples.BlueLibConstants;
import software.bluelib_examples.entity.ExampleEntity;

public class EntityRegistry {

    public static void init() {}

    public static final Supplier<EntityType<ExampleEntity>> EXAMPLE = registerEntity("example", ExampleEntity::new, 0.45f, 1f, 0x5F2A31, 0x6F363E);

    public static void registerEntityAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> registrar) {
        AttributeSupplier.Builder genericAttribs = PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MAX_HEALTH, 1);

        registrar.accept(EntityRegistry.EXAMPLE.get(), genericAttribs.build());
    }

    private static <T extends Mob> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entity, float width, float height, int primaryEggColor, int secondaryEggColor) {
        return BlueLibConstants.PLATFORM.registerEntity(name, () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));
    }
}
