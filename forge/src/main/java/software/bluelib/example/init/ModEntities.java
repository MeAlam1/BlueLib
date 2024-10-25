// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;

/**
 * A {@code public class} for registering {@link EntityType} for this mod.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #register(IEventBus)} - Registers the {@link EntityType} for this mod. </li>
 * </ul>
 */
public class ModEntities {

    /**
     * A {@code public static final} {@link DeferredRegister} of {@link EntityType} for this mod.
     *
     * @since 1.0.0
     */
    public static final DeferredRegister<EntityType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BlueLibConstants.MOD_ID);

    /**
     * A {@code public static final} {@link RegistryObject} of {@link EntityType} for the {@link DragonEntity}.
     *
     * @since 1.0.0
     */
    public static final RegistryObject<EntityType<DragonEntity>> DRAGON =
            REGISTER.register("example_one", () -> EntityType.Builder.of(DragonEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f)
                    .build(ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "dragon").toString()));

    /**
     * A {@code public static final} {@link RegistryObject} of {@link EntityType} for the {@link RexEntity}.
     *
     * @since 1.0.0
     */
    public static final RegistryObject<EntityType<RexEntity>> REX =
            REGISTER.register("example_two", () -> EntityType.Builder.of(RexEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f)
                    .build(ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "rex").toString()));

    /**
     * A {@code public static} method to register the {@link EntityType} for this mod.
     * @param pEventBus {@link IEventBus} - The event bus to register the {@link EntityType} with.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public static void register(IEventBus pEventBus) {
        REGISTER.register(pEventBus);
    }
}
