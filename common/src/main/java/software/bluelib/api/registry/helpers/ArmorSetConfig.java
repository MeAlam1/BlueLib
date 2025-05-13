package software.bluelib.api.registry.helpers;

import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ArmorSetConfig {

    public Consumer<Item.Properties> helmetProperties;
    public Consumer<Item.Properties> chestplateProperties;
    public Consumer<Item.Properties> leggingsProperties;
    public Consumer<Item.Properties> bootsProperties;

    public ArmorSetConfig helmet(Consumer<Item.Properties> properties) {
        this.helmetProperties = properties;
        return this;
    }

    public ArmorSetConfig chestplate(Consumer<Item.Properties> properties) {
        this.chestplateProperties = properties;
        return this;
    }

    public ArmorSetConfig leggings(Consumer<Item.Properties> properties) {
        this.leggingsProperties = properties;
        return this;
    }

    public ArmorSetConfig boots(Consumer<Item.Properties> properties) {
        this.bootsProperties = properties;
        return this;
    }
}
