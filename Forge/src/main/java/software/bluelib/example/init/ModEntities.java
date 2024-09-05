// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bluelib.BlueLib;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;

@Mod.EventBusSubscriber(modid = BlueLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.ENTITIES, BlueLib.MODID);

    // List of Entities
    public static final RegistryObject<EntityType<DragonEntity>> DRAGON =
            REGISTER.register("dragon", () -> EntityType.Builder.of(DragonEntity::new, EntityClassification.CREATURE)
                    .sized(0.6f, 1.8f)
                    .build(new ResourceLocation(BlueLib.MODID, "dragon").toString()));

    public static final RegistryObject<EntityType<RexEntity>> REX =
            REGISTER.register("rex", () -> EntityType.Builder.of(RexEntity::new, EntityClassification.CREATURE)
                    .sized(0.6f, 1.8f)
                    .build(new ResourceLocation(BlueLib.MODID, "rex").toString()));

    public static void register(IEventBus pEventBus) {
        REGISTER.register(pEventBus);
    }
}