package software.bluelib.api.registry.builders.tabs;

import java.util.*;
import java.util.function.Supplier;
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
        return () -> {
            EntityType<?> entityType = entityTypeSupplier.get();
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
                    Set<Item> addedItems = new HashSet<>(); // Track added items
                    if (displayItemsGenerator != null) {
                        displayItemsGenerator.accept(parameters, (stack, tabVisibility) -> {
                            Item item = stack.getItem();
                            if (item != null && addedItems.add(item)) {
                                output.accept(stack, tabVisibility);
                                BaseLogger.log(BaseLogLevel.INFO, "Adding item from displayItemsGenerator: " + BuiltInRegistries.ITEM.getKey(item));
                            }
                        });
                    }
                    Item item = iconSupplier.get();
                    if (item != null) {
                        List<Supplier<Item>> toolsetItems = ItemBuilder.TOOLSETS.get(item.getDescriptionId());
                        if (toolsetItems != null) {
                            for (Supplier<Item> tool : toolsetItems) {
                                Item toolItem = tool.get();
                                if (toolItem != null && addedItems.add(toolItem)) {
                                    output.accept(new ItemStack(toolItem), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                                    BaseLogger.log(BaseLogLevel.INFO, "Adding icon toolset item: " + BuiltInRegistries.ITEM.getKey(toolItem));
                                }
                            }
                        } else if (addedItems.add(item)) {
                            output.accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                            BaseLogger.log(BaseLogLevel.INFO, "Adding icon item: " + BuiltInRegistries.ITEM.getKey(item));
                        }
                    }
                });

        CreativeModeTab tab = tabBuilder.build();
        Supplier<CreativeModeTab> tabSupplier = () -> tab;
        TAB_BUILDERS.put(tabSupplier, this);
        BlueLibConstants.PlatformHelper.REGISTRY.registerTab(id, tabSupplier);
        return tabSupplier;
    }

    public static void addToolset(Item item, CreativeModeTab.Output populator) {
        if (item != null) {
            Set<Item> addedItems = new HashSet<>(); // Track added items
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
                    Item toolItem = tool.get();
                    if (toolItem != null && addedItems.add(toolItem)) { // Only add if not already present
                        populator.accept(new ItemStack(toolItem), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        BaseLogger.log(BaseLogLevel.INFO, "Adding toolset item: " + BuiltInRegistries.ITEM.getKey(toolItem));
                    }
                }
                return;
            }

            if (addedItems.add(item)) { // Only add if not already present
                populator.accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                BaseLogger.log(BaseLogLevel.INFO, "Adding single toolset item: " + fullId);
            }
        }
    }

    public static void addArmorSet(Item item, CreativeModeTab.Output populator) {
        if (item != null) {
            Set<Item> addedItems = new HashSet<>(); // Track added items
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
                    Item armorItem = armor.get();
                    if (armorItem != null && addedItems.add(armorItem)) { // Only add if not already present
                        populator.accept(new ItemStack(armorItem), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        BaseLogger.log(BaseLogLevel.INFO, "Adding armor item: " + BuiltInRegistries.ITEM.getKey(armorItem));
                    }
                }
                return;
            }

            if (addedItems.add(item)) { // Only add if not already present
                populator.accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                BaseLogger.log(BaseLogLevel.INFO, "Adding single armor item: " + fullId);
            }
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
