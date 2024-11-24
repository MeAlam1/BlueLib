// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;

/**
 * A {@code public class} for registering {@link EntityType} for this mod.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #register(String, EntityType.Builder)} - Registers an {@link EntityType} with the specified {@code pRegistryName} and {@code pEntityTypeBuilder}. </li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class ModEntities {

    /**
     * A {@code public static final} {@link DeferredRegister} of {@link EntityType} for this mod.
     *
     * @since 1.0.0
     */
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, BlueLibConstants.MOD_ID);

    /**
     * A {@code public static final} {@link DeferredHolder} of {@link EntityType} for the {@link DragonEntity}.
     *
     * @since 1.0.0
     */
    public static final DeferredHolder<EntityType<?>, EntityType<DragonEntity>> DRAGON = register(
            "example_one",
            EntityType.Builder.of(DragonEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f));

    /**
     * A {@code public static final} {@link DeferredHolder} of {@link EntityType} for the {@link RexEntity}.
     *
     * @since 1.0.0
     */
    public static final DeferredHolder<EntityType<?>, EntityType<RexEntity>> REX = register(
            "example_two",
            EntityType.Builder.of(RexEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f));

    /**
     * A {@code private static} {@link Entity} method to register an {@link EntityType} with the specified {@code pRegistryName} and {@code pEntityTypeBuilder}.
     *
     * @param pRegistryName      {@link String} - The registry name of the {@link EntityType}.
     * @param pEntityTypeBuilder {@link EntityType.Builder} - The builder of the {@link EntityType}.
     * @param <T>                The type of the entity.
     * @return {@link DeferredHolder<EntityType>} - The deferred holder of the {@link EntityType}.
     * @author MeAlam
     * @since 1.0.0
     */
    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String pRegistryName, EntityType.Builder<T> pEntityTypeBuilder) {
        ResourceLocation registryLocation = ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pRegistryName);
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, registryLocation);

        return REGISTRY.register(pRegistryName, () -> pEntityTypeBuilder.build(resourceKey));
    }
}
