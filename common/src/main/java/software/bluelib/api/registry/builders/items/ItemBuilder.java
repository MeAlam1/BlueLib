package software.bluelib.api.registry.builders.items;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.datagen.RecipeGenerator;
import software.bluelib.api.registry.datagen.items.ItemModelGenerator;
import software.bluelib.api.registry.datagen.items.ItemModelTemplates;
import software.bluelib.api.registry.helpers.ArmorSetConfig;
import software.bluelib.api.registry.helpers.ToolsetConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ItemBuilder<T extends Item> {

    public static final List<String> generatedItems = new ArrayList<>();
    public static final Map<String, ItemModelTemplates> customModelMap = new HashMap<>();
    public static final List<ItemBuilder<?>> REGISTERED_BUILDERS = new ArrayList<>();
    public static String itemName;
    public final Function<Item.Properties, T> itemConstructor;
    public Consumer<Item.Properties> propertiesConsumer = props -> {};
    public static final Map<String, List<Supplier<Item>>> TOOLSETS = new HashMap<>();
    public static final Map<String, List<Supplier<Item>>> ARMORSETS = new HashMap<>();
    private static final String modId = AbstractRegistryBuilder.getModID();
    private BiConsumer<RecipeContext, RecipeOutput> recipeConsumer;
    private T registeredItem;

    public ItemBuilder(String name, Function<Item.Properties, T> itemConstructor) {
        itemName = name;
        this.itemConstructor = itemConstructor;
    }

    public static <T extends Item> ItemBuilder<T> item(String name, Function<Item.Properties, T> itemConstructor) {
        return new ItemBuilder<>(name, itemConstructor);
    }

    public static void doItemModelGen(String modId) {
        for (String itemName : generatedItems) {
            ItemModelTemplates template = customModelMap.getOrDefault(itemName, ItemModelTemplates.HANDHELD);

            if (itemName.endsWith("_sword") || itemName.endsWith("_pickaxe") || itemName.endsWith("_axe") || itemName.endsWith("_shovel") || itemName.endsWith("_hoe")) {
                template = ItemModelTemplates.HANDHELD;
            } else if (itemName.endsWith("_helmet") || itemName.endsWith("_chestplate") || itemName.endsWith("_leggings") || itemName.endsWith("_boots")) {
                template = ItemModelTemplates.GENERATED;
            }

            ItemModelGenerator.generateItemModel(modId, itemName, template);
        }
    }

    public static void doRecipeGen(String modId) {
        for (ItemBuilder<?> builder : REGISTERED_BUILDERS) {
            if (builder.registeredItem != null && builder.recipeConsumer != null) {
                RecipeGenerator.generateRecipe(modId, itemName, (jsonConsumer, jsonSupplier) -> {
                    RecipeContext ctx = new RecipeContext(builder.registeredItem);
                    builder.recipeConsumer.accept(ctx, jsonConsumer);
                });
            }
        }
    }

    public ItemBuilder<T> properties(Consumer<Item.Properties> consumer) {
        this.propertiesConsumer = consumer;
        return this;
    }

    public ItemBuilder<T> model(ItemModelTemplates template) {
        customModelMap.put(itemName, template);
        return this;
    }

    public ItemBuilder<T> recipe(BiConsumer<RecipeContext, RecipeOutput> recipeConsumer) {
        this.recipeConsumer = recipeConsumer;
        return this;
    }

    private <U extends Item> Supplier<U> registerTool(String toolName, Function<Item.Properties, U> toolConstructor, Consumer<Item.Properties> toolProperties) {
        Item.Properties properties = new Item.Properties();
        propertiesConsumer.accept(properties);
        if (toolProperties != null) {
            toolProperties.accept(properties);
        }
        Supplier<U> itemSupplier = () -> toolConstructor.apply(properties);
        BlueLibConstants.PlatformHelper.REGISTRY.registerItem(toolName, (Supplier<Item>) itemSupplier);
        generatedItems.add(toolName);

        return itemSupplier;
    }

    public ItemBuilder<SwordItem> sword(Consumer<Item.Properties> swordProperties, Tier tier, int attackDamage, float attackSpeed) {
        String toolName = itemName + "_sword";
        registerTool(toolName, props -> new SwordItem(tier, props.attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed))), swordProperties);
        return (ItemBuilder<SwordItem>) this;
    }

    public ItemBuilder<PickaxeItem> pickaxe(Consumer<Item.Properties> pickaxeProperties, Tier tier, int attackDamage, float attackSpeed) {
        String toolName = itemName + "_pickaxe";
        registerTool(toolName, props -> new PickaxeItem(tier, props.attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed))), pickaxeProperties);
        return (ItemBuilder<PickaxeItem>) this;
    }

    public ItemBuilder<AxeItem> axe(Consumer<Item.Properties> axeProperties, Tier tier, int attackDamage, float attackSpeed) {
        String toolName = itemName + "_axe";
        registerTool(toolName, props -> new AxeItem(tier, props.attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed))), axeProperties);
        return (ItemBuilder<AxeItem>) this;
    }

    public ItemBuilder<ShovelItem> shovel(Consumer<Item.Properties> shovelProperties, Tier tier, int attackDamage, float attackSpeed) {
        String toolName = itemName + "_shovel";
        registerTool(toolName, props -> new ShovelItem(tier, props.attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed))), shovelProperties);
        return (ItemBuilder<ShovelItem>) this;
    }

    public ItemBuilder<HoeItem> hoe(Consumer<Item.Properties> hoeProperties, Tier tier, int attackDamage, float attackSpeed) {
        String toolName = itemName + "_hoe";
        registerTool(toolName, props -> new HoeItem(tier, props.attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed))), hoeProperties);
        return (ItemBuilder<HoeItem>) this;
    }

    public ItemBuilder<T> toolset(Tier tier, ToolsetConfig config) {
        if (config.swordProperties != null) {
            sword(config.swordProperties, tier, config.swordAttackDamage, config.swordAttackSpeed);
        }
        if (config.pickaxeProperties != null) {
            pickaxe(config.pickaxeProperties, tier, config.pickaxeAttackDamage, config.pickaxeAttackSpeed);
        }
        if (config.axeProperties != null) {
            axe(config.axeProperties, tier, config.axeAttackDamage, config.axeAttackSpeed);
        }
        if (config.shovelProperties != null) {
            shovel(config.shovelProperties, tier, config.shovelAttackDamage, config.shovelAttackSpeed);
        }
        if (config.hoeProperties != null) {
            hoe(config.hoeProperties, tier, config.hoeAttackDamage, config.hoeAttackSpeed);
        }

        TOOLSETS.put(itemName, generatedItems.stream()
                .map(itemName -> (Supplier<Item>) () -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(modId, itemName)))
                .collect(Collectors.toList()));

        return this;
    }

    public ItemBuilder<ArmorItem> helmet(Consumer<Item.Properties> properties, Holder<ArmorMaterial> material) {
        String armorName = itemName + "_helmet";
        registerTool(armorName, props -> new ArmorItem(material, ArmorItem.Type.HELMET, props), properties);
        return (ItemBuilder<ArmorItem>) this;
    }

    public ItemBuilder<ArmorItem> chestplate(Consumer<Item.Properties> properties, Holder<ArmorMaterial> material) {
        String armorName = itemName + "_chestplate";
        registerTool(armorName, props -> new ArmorItem(material, ArmorItem.Type.CHESTPLATE, props), properties);
        return (ItemBuilder<ArmorItem>) this;
    }

    public ItemBuilder<ArmorItem> leggings(Consumer<Item.Properties> properties, Holder<ArmorMaterial> material) {
        String armorName = itemName + "_leggings";
        registerTool(armorName, props -> new ArmorItem(material, ArmorItem.Type.LEGGINGS, props), properties);
        return (ItemBuilder<ArmorItem>) this;
    }

    public ItemBuilder<ArmorItem> boots(Consumer<Item.Properties> properties, Holder<ArmorMaterial> material) {
        String armorName = itemName + "_boots";
        registerTool(armorName, props -> new ArmorItem(material, ArmorItem.Type.BOOTS, props), properties);
        return (ItemBuilder<ArmorItem>) this;
    }

    public ItemBuilder<T> armorSet(Holder<ArmorMaterial> material, ArmorSetConfig config) {
        if (config.helmetProperties != null)
            helmet(config.helmetProperties, material);
        if (config.chestplateProperties != null)
            chestplate(config.chestplateProperties, material);
        if (config.leggingsProperties != null)
            leggings(config.leggingsProperties, material);
        if (config.bootsProperties != null)
            boots(config.bootsProperties, material);

        ARMORSETS.put(itemName, generatedItems.stream()
                .map(name -> (Supplier<Item>) () -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(modId, name)))
                .collect(Collectors.toList()));

        return this;
    }

    public Supplier<Item> register() {
        Item.Properties properties = new Item.Properties();
        propertiesConsumer.accept(properties);
        generatedItems.add(itemName);
        Supplier<Item> itemSupplier = () -> {
            T item = itemConstructor.apply(properties);
            this.registeredItem = item;
            return item;
        };
        BlueLibConstants.PlatformHelper.REGISTRY.registerItem(itemName, itemSupplier);
        REGISTERED_BUILDERS.add(this);
        return itemSupplier;
    }

    public static class RecipeContext {
        private final Item item;

        public RecipeContext(Item item) {
            this.item = item;
        }

        public Item getEntry() {
            return item;
        }
    }
}