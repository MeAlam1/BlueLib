package software.bluelib.api.registry.builders.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.datagen.items.ItemModelGenerator;
import software.bluelib.api.registry.datagen.items.ItemModelTemplates;
import software.bluelib.api.registry.helpers.entity.AttributeHelper;
import software.bluelib.api.registry.helpers.entity.RenderHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EntityBuilder<T extends Mob> {

    public static String name;
    public final EntityType.EntityFactory<T> factory;
    public final MobCategory category;
    public float width = 0.6f;
    public float height = 1.8f;
    public static boolean hasSpawnEgg = false;
    public int primaryEggColor;
    public int secondaryEggColor;
    public Supplier<AttributeSupplier.Builder> attributeBuilder = null;
    public EntityRendererProvider<T> rendererProvider = null;
    public boolean hasVariants = false;
    public static final List<String> DRAGON_NAMES = new ArrayList<>();
    public Supplier<CreativeModeTab> tabSupplier = null;
    public static final Map<Supplier<CreativeModeTab>, List<Supplier<Item>>> SPAWN_EGGS_BY_TAB = new HashMap<>();

    public EntityBuilder(String name, EntityType.EntityFactory<T> factory, MobCategory category) {
        EntityBuilder.name = name;
        this.factory = factory;
        this.category = category;
    }

    public EntityBuilder<T> sized(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public EntityBuilder<T> spawnEgg(int primaryColor, int secondaryColor) {
        hasSpawnEgg = true;
        this.primaryEggColor = primaryColor;
        this.secondaryEggColor = secondaryColor;
        return this;
    }

    public EntityBuilder<T> attributes(Supplier<AttributeSupplier.Builder> attributes) {
        this.attributeBuilder = attributes;
        return this;
    }

    public EntityBuilder<T> renderer(EntityRendererProvider<T> rendererProvider) {
        this.rendererProvider = rendererProvider;
        return this;
    }

    public EntityBuilder<T> loadVariants() {
        this.hasVariants = true;
        return this;
    }

    public EntityBuilder<T> tab(Supplier<CreativeModeTab> tabSupplier) {
        this.tabSupplier = tabSupplier;
        return this;
    }

    public static List<Supplier<Item>> getSpawnEggsForTab(CreativeModeTab tab) {
        List<Supplier<Item>> spawnEggs = new ArrayList<>();
        for (Map.Entry<Supplier<CreativeModeTab>, List<Supplier<Item>>> entry : SPAWN_EGGS_BY_TAB.entrySet()) {
            if (entry.getKey().get() == tab) {
                spawnEggs.addAll(entry.getValue());
            }
        }
        return spawnEggs;
    }

    public Supplier<EntityType<T>> register() {
        Supplier<EntityType<T>> entityTypeSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerEntity(name, () -> EntityType.Builder.of(factory, category)
                .sized(width, height)
                .build(name));

        if (hasSpawnEgg) {
            registerSpawnEgg(name, entityTypeSupplier, primaryEggColor, secondaryEggColor, tabSupplier);
        }

        if (attributeBuilder != null) {
            AttributeHelper.queueAttributes(entityTypeSupplier, attributeBuilder);
        }

        if (rendererProvider != null) {
            RenderHelper.queueRenderer((entityConsumer, blockConsumer) -> {
                entityConsumer.accept(entityTypeSupplier.get(), rendererProvider);
            });
        }

        if (hasVariants) {
            DRAGON_NAMES.add(name);
        }

        return entityTypeSupplier;
    }

    public static void doSpawnEggDatagen(String modId) {
        if (hasSpawnEgg) {
            String spawnEggName = name + "_spawn_egg";
            ItemModelGenerator.generateItemModel(modId, spawnEggName, ItemModelTemplates.SPAWN_EGG);
        }
    }

    public static List<String> getDragonNames() {
        return List.copyOf(DRAGON_NAMES);
    }

    public Supplier<Item> registerSpawnEgg(String name, Supplier<EntityType<T>> entityType, int primaryColor, int secondaryColor, Supplier<CreativeModeTab> tabSupplier) {
        Supplier<Item> spawnEggSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerItem(name + "_spawn_egg", () -> new SpawnEggItem(
                entityType.get(),
                primaryColor,
                secondaryColor,
                new Item.Properties()));
        if (tabSupplier != null) {
            SPAWN_EGGS_BY_TAB.computeIfAbsent(tabSupplier, k -> new ArrayList<>()).add(spawnEggSupplier);
        }
        return spawnEggSupplier;
    }
}
