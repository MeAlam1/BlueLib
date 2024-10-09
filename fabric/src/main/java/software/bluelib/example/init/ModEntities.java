package software.bluelib.example.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;

import static net.minecraft.world.entity.MobCategory.CREATURE;

public class ModEntities {

    public static EntityType<DragonEntity> EXAMPLE_ONE;
    public static EntityType<RexEntity> EXAMPLE_TWO;

    public static void initializeEntities() {
        EXAMPLE_ONE = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "example_one"),
                EntityType.Builder.of(DragonEntity::new, CREATURE)
                        .sized(0.6F, 1.8F)
                        .build("example_one"));

        EXAMPLE_TWO = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "example_two"),
                EntityType.Builder.of(RexEntity::new, CREATURE)
                        .sized(0.6F, 1.8F)
                        .build("example_two"));
    }
}
