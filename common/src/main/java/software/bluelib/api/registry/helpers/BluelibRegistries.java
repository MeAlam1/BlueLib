package software.bluelib.api.registry.helpers;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import software.bluelib.api.registry.builders.RegistryBuilder;

public class BluelibRegistries extends BuiltInRegistries {
    public static final Registry<Biome> BIOME = registerSimple(Registries.BIOME, (p_259341_) -> Biomes.PLAINS);
    public static final Registry<Item> BITEM = registerSimple(Registries.ITEM, (p_259341_) -> Items.AIR);
}
