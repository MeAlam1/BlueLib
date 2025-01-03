// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib_examples.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import software.bluelib_examples.BlueLibConstants;
import software.bluelib_examples.entity.ExampleEntity;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BlueLibConstants.MOD_ID);

    public static final Supplier<EntityType<ExampleEntity>> EXAMPLE = ENTITY_TYPES.register("example", () -> EntityType.Builder.of(ExampleEntity::new, MobCategory.MONSTER)
            .sized(0.75f, 0.35f)
            .build(ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(),
                    ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "example"))));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
