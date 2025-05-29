package software.bluelib.api.registry.builders.tabs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.builders.entity.EntityBuilder;
import software.bluelib.api.registry.builders.items.ItemBuilder;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CreativeTabBuilder {

    private final String id;
    private static String modId = AbstractRegistryBuilder.getModID();
    private Supplier<Item> iconSupplier;
    private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator;
    private String backgroundSuffix;
    private static final Map<Supplier<CreativeModeTab>, CreativeTabBuilder> TAB_BUILDERS = new HashMap<>();

    public CreativeTabBuilder(String id) {
        this.id = id;

    }

    public CreativeTabBuilder icon(Supplier<Item> iconSupplier) {
        this.iconSupplier = iconSupplier;
        return this;
    }

    public CreativeTabBuilder icon(Item iconItem) {
        this.iconSupplier = () -> iconItem;
        return this;
    }

    public static Supplier<Item> useSpawnEgg(Supplier<? extends EntityType<?>> entityTypeSupplier) {
        return () -> {EntityType<?> entityType = entityTypeSupplier.get();
            if (entityType != null) {
                String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
                String spawnEggId = entityId + "_spawn_egg";
                return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(modId, spawnEggId));
            }
            return Items.AIR;
        };
    }

    public CreativeTabBuilder displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        this.displayItemsGenerator = displayItemsGenerator;
        return this;
    }

    public CreativeTabBuilder background(String backgroundSuffix) {
        this.backgroundSuffix = backgroundSuffix;
        return this;
    }

    public Supplier<CreativeModeTab> register() {
        CreativeModeTab.Builder tabBuilder = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable(id))
                .icon(() -> {
                    Item item = iconSupplier.get();
                    return item != null ? new ItemStack(item) : ItemStack.EMPTY;
                })
                .displayItems((parameters, output) -> {
                    if (displayItemsGenerator != null) {
                        displayItemsGenerator.accept(parameters, output);
                    }

                    Item item = iconSupplier.get();
                    if (item != null) {
                        List<Supplier<Item>> toolsetItems = ItemBuilder.TOOLSETS.get(item.getDescriptionId());
                        if (toolsetItems != null) {
                            for (Supplier<Item> tool : toolsetItems) {
                                output.accept(tool.get());
                            }
                        } else {
                            output.accept(item);
                        }
                    }
                })
                .backgroundTexture(ResourceLocation.fromNamespaceAndPath(modId, backgroundSuffix));

        CreativeModeTab tab = tabBuilder.build();
        Supplier<CreativeModeTab> tabSupplier = () -> tab;
        TAB_BUILDERS.put(tabSupplier, this);
        BlueLibConstants.PlatformHelper.REGISTRY.registerTab(id, tabSupplier);
        return tabSupplier;
    }

    public static void addToolset(Item item, CreativeModeTab.Output populator) {
        if (item != null) {
            String fullId = BuiltInRegistries.ITEM.getKey(item).getPath();

            String[] toolSuffixes = { "_sword", "_pickaxe", "_axe", "_shovel", "_hoe" };
            String baseId = fullId;
            for (String suffix : toolSuffixes) {
                if (fullId.endsWith(suffix)) {
                    baseId = fullId.substring(0, fullId.length() - suffix.length());
                    break;
                }
            }

            List<Supplier<Item>> toolset = ItemBuilder.TOOLSETS.get(baseId);
            if (toolset != null) {
                for (Supplier<Item> tool : toolset) {
                    populator.accept(tool.get());
                }
                return;
            }

            populator.accept(item);
        }
    }

    public static void addArmorSet(Item item, CreativeModeTab.Output populator) {
        if (item != null) {
            String fullId = BuiltInRegistries.ITEM.getKey(item).getPath();

            String[] toolSuffixes = { "_helmet", "_chestplate", "_leggings", "_boots" };
            String baseId = fullId;
            for (String suffix : toolSuffixes) {
                if (fullId.endsWith(suffix)) {
                    baseId = fullId.substring(0, fullId.length() - suffix.length());
                    break;
                }
            }

            List<Supplier<Item>> armorSets = ItemBuilder.ARMORSETS.get(baseId);
            if (armorSets != null) {
                for (Supplier<Item> armor : armorSets) {
                    populator.accept(armor.get());
                }
                return;
            }

            populator.accept(item);
        }
    }

    public static void addSpawnEgg(EntityType<?> entityType, CreativeModeTab.Output populator) {
        if (entityType != null) {
            String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
            String spawnEggId = entityId + "_spawn_egg";
            Item spawnEggItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(modId, spawnEggId));
            populator.accept(spawnEggItem);
        }
    }

    public static void addAllSpawnEggs(CreativeModeTab.Output populator) {
        List<String> names = EntityBuilder.getDragonNames();
        for (String name : names) {
            String spawnEggId = name + "_spawn_egg";
            try {
                Item spawnEggItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(modId, spawnEggId));
                populator.accept(spawnEggItem);
            } catch (Exception e) {
                BaseLogger.log(BaseLogLevel.ERROR, Component.literal("Spawn egg for entity " + name + " not found!"));
            }
        }
    }
}
