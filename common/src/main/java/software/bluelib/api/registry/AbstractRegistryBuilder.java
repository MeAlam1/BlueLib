package software.bluelib.api.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import software.bluelib.api.registry.builders.blocks.BlockBuilder;
import software.bluelib.api.registry.builders.blocks.BlockEntityBuilder;
import software.bluelib.api.registry.builders.entity.EntityBuilder;
import software.bluelib.api.registry.builders.entity.ProjectileBuilder;
import software.bluelib.api.registry.builders.items.ItemBuilder;
import software.bluelib.api.registry.builders.keybinds.KeybindBuilder;
import software.bluelib.api.registry.builders.tabs.CreativeTabBuilder;
import software.bluelib.api.registry.datagen.entity.EntityTagBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractRegistryBuilder {
    private static String modID;

    public AbstractRegistryBuilder(String modId) {
        modID = modId;
    }

    public static void setModID(String modId) {
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
        return new BlockBuilder<>(name, blockFactory);
    }

    public static <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String name, BlockEntityType.BlockEntitySupplier<T> factory) {
        return new BlockEntityBuilder<>(name, factory);
    }

    public <T extends Item> ItemBuilder<T> item(String name, Function<Item.Properties, T> constructor) {
        return new ItemBuilder<>(name, constructor);
    }

    public CreativeTabBuilder tab(String id) {
        return new CreativeTabBuilder(id);
    }

    public static KeybindBuilder keybind(String name, int keyCode) {
        return KeybindBuilder.keybind(name, keyCode);
    }

    public static void doDatagen() {
        ItemBuilder.doItemModelGen(modID);
        BlockBuilder.doBlockModelGen(modID);
        EntityBuilder.doSpawnEggDatagen(modID);
        EntityTagBuilder.doTagJsonGen();
    }
}