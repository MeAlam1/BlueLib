// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluetest.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bluetest.BlueTest;
import software.bluetest.entity.dragon.DragonEntity;
import software.bluetest.entity.rex.RexEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BlueTest.MODID);

    // List of Entities
    public static final RegistryObject<EntityType<DragonEntity>> DRAGON =
            REGISTER.register("dragon", () -> EntityType.Builder.of(DragonEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f)
                    .build(new ResourceLocation(BlueTest.MODID, "dragon").toString()));

    public static final RegistryObject<EntityType<RexEntity>> REX =
            REGISTER.register("rex", () -> EntityType.Builder.of(RexEntity::new, MobCategory.AMBIENT)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(64)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .sized(0.6f, 1.8f)
                    .build(new ResourceLocation(BlueTest.MODID, "rex").toString()));

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
