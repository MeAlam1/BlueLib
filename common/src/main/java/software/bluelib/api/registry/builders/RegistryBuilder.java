package software.bluelib.api.registry.builders;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import software.bluelib.api.registry.builders.blocks.BlockBuilder;
import software.bluelib.api.registry.builders.entity.EntityBuilder;
import software.bluelib.api.registry.builders.entity.ProjectileBuilder;
import software.bluelib.api.registry.builders.items.ItemBuilder;
import software.bluelib.api.registry.builders.tabs.CreativeTabBuilder;

import java.util.function.Function;

public class RegistryBuilder {

    public static String modID;

    public RegistryBuilder(String modId) {
        modID = modId;
    }

    public static String getModID() {
        return modID;
    }

    public <T extends Mob> EntityBuilder<T> entity(String name, EntityType.EntityFactory<T> factory, MobCategory category) {
        return new EntityBuilder<>(name, factory, category);
    }

    public <T extends Entity> ProjectileBuilder<T> projectile(String name, EntityType.EntityFactory<T> factory, MobCategory category, Class<T> entityClass) {
        return new ProjectileBuilder<>(name, factory, category, entityClass);
    }

    public <T extends Block> BlockBuilder<T> block(String name, Function<Block.Properties, T> blockFactory) {
        return new BlockBuilder<>(getModID(), name, blockFactory);
    }

    public <T extends Item> ItemBuilder<T> item(String name, Function<Item.Properties, T> constructor) {
        return new ItemBuilder<>(getModID(), name, constructor);
    }

    public CreativeTabBuilder tab(String id) {
        return new CreativeTabBuilder(id, getModID());
    }

    public static void doDatagen() {
        ItemBuilder.doItemModelGen(getModID());
        BlockBuilder.doBlockModelGen(getModID());
        EntityBuilder.doSpawnEggDatagen(getModID());
    }
}
