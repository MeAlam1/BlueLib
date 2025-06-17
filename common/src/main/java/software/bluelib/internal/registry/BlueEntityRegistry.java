/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.internal.registry;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.ExampleEntity;

@ApiStatus.Internal
public class BlueEntityRegistry {

    public static void init() {}

    public static final Supplier<EntityType<ExampleEntity>> EXAMPLE = registerEntity("example", ExampleEntity::new, 0.45f, 1f, 0x5F2A31, 0x6F363E);

    public static void registerEntityAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> registrar) {
        AttributeSupplier.Builder genericAttribs = PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MAX_HEALTH, 1);

        registrar.accept(BlueEntityRegistry.EXAMPLE.get(), genericAttribs.build());
    }

    private static <T extends Mob> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entity, float width, float height, int primaryEggColor, int secondaryEggColor) {
        return BlueLibConstants.PlatformHelper.REGISTRY.registerEntity(name, () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));
    }
}
