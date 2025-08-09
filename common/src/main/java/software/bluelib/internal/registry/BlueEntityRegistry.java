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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.config.LoggerConfig;
import software.bluelib.example.entity.ExampleEntity;

@ApiStatus.Internal
public class BlueEntityRegistry {

	public static void init() {}

	@Nullable
	public static final Supplier<EntityType<ExampleEntity>> EXAMPLE = registerEntity("example", ExampleEntity::new, 0.45f, 1f, 0x5F2A31, 0x6F363E);

	public static void registerEntityAttributes(@NotNull BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> pRegistrar) {
		AttributeSupplier.Builder genericAttribs = PathfinderMob.createMobAttributes()
				.add(Attributes.FOLLOW_RANGE, 16)
				.add(Attributes.MAX_HEALTH, 1);

		if (LoggerConfig.isExampleEnabled) {
			pRegistrar.accept(BlueEntityRegistry.EXAMPLE.get(), genericAttribs.build());
		}
	}

	private static <T extends Mob> @Nullable Supplier<EntityType<T>> registerEntity(@NotNull String pName, @NotNull EntityType.EntityFactory<T> pEntity, @NotNull Float pWidth, @NotNull Float pHeight, @NotNull Integer pPrimaryEggColor, @NotNull Integer pSecondaryEggColor) {
		if (LoggerConfig.isExampleEnabled) {
			return BlueLibConstants.PlatformHelper.REGISTRY.registerEntity(pName, () -> EntityType.Builder.of(pEntity, MobCategory.CREATURE).sized(pWidth, pHeight).build(pName));
		}
		return null;
	}
}
