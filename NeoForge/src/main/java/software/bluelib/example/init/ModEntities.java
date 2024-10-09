// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, BlueLibConstants.MOD_ID);

    // List of Entities
    public static final DeferredHolder<EntityType<?>, EntityType<DragonEntity>> DRAGON = register(
            "example_one",
            EntityType.Builder.of(DragonEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f));

    public static final DeferredHolder<EntityType<?>, EntityType<RexEntity>> REX = register(
            "example_two",
            EntityType.Builder.of(RexEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f));

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String pRegistryName, EntityType.Builder<T> pEntityTypeBuilder) {
        return REGISTRY.register(pRegistryName, () -> pEntityTypeBuilder.build(pRegistryName));
    }
}
