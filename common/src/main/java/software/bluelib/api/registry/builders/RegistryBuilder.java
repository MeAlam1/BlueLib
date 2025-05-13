package software.bluelib.api.registry.builders;

import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import software.bluelib.api.registry.builders.blocks.*;
import software.bluelib.api.registry.builders.entity.*;
import software.bluelib.api.registry.builders.items.*;
import software.bluelib.api.registry.builders.tabs.*;

public class RegistryBuilder {

    public static <T extends Mob> EntityBuilder<T> entity(String name, EntityType.EntityFactory<T> factory, MobCategory category) {
        return new EntityBuilder<>(name, factory, category);
    }

    public static <T extends Entity> ProjectileBuilder<T> projectile(String name, EntityType.EntityFactory<T> factory, MobCategory category, Class<T> entityClass) {
        return new ProjectileBuilder<>(name, factory, category, entityClass);
    }

    public static <T extends Block> BlockBuilder<T> block(String modId, String name, Function<Block.Properties, T> blockFactory) {
        return new BlockBuilder<>(modId, name, blockFactory);
    }

    public static <T extends Item> ItemBuilder<T> item(String modId, String name, Function<Item.Properties, T> constructor) {
        return new ItemBuilder<>(modId, name, constructor);
    }

    public static CreativeTabBuilder tab(String id, String modId) {
        return new CreativeTabBuilder(id, modId);
    }
}
