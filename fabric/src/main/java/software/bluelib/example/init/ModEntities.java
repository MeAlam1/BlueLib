// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;

import static net.minecraft.world.entity.MobCategory.CREATURE;

/**
 * A {@code public class} that contains the entities for the mod.
 * <p>
 * Key Methods:
 * <ul>
 *     <li>{@link #initializeEntities()} - Initializes the entities.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class ModEntities {

    /**
     * The {@code public static} field that stores Example One.
     */
    public static EntityType<DragonEntity> EXAMPLE_ONE;

    /**
     * The {@code public static} field that stores Example Two.
     */
    public static EntityType<RexEntity> EXAMPLE_TWO;

    /**
     * A {@code public static void} that initializes the entities.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public static void initializeEntities() {
        EXAMPLE_ONE = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "example_one"),
                EntityType.Builder.of(DragonEntity::new, CREATURE)
                        .sized(0.6F, 1.8F)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "example_one"))));

        EXAMPLE_TWO = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "example_two"),
                EntityType.Builder.of(RexEntity::new, CREATURE)
                        .sized(0.6F, 1.8F)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "example_two"))));
    }
}
