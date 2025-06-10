package software.bluelib.api.registry.helpers;

import java.util.function.Consumer;
import net.minecraft.world.item.Item;

public class ToolsetConfig {

    public Consumer<Item.Properties> swordProperties;
    public Consumer<Item.Properties> pickaxeProperties;
    public Consumer<Item.Properties> axeProperties;
    public Consumer<Item.Properties> shovelProperties;
    public Consumer<Item.Properties> hoeProperties;

    public int swordAttackDamage = 3;
    public float swordAttackSpeed = -2.4F;
    public int pickaxeAttackDamage = 1;
    public float pickaxeAttackSpeed = -2.8F;
    public int axeAttackDamage = 6;
    public float axeAttackSpeed = -3.0F;
    public int shovelAttackDamage = 1;
    public float shovelAttackSpeed = -3.0F;
    public int hoeAttackDamage = 0;
    public float hoeAttackSpeed = -3.0F;

    public ToolsetConfig sword(int attackDamage, float attackSpeed, Consumer<Item.Properties> properties) {
        this.swordAttackDamage = attackDamage;
        this.swordAttackSpeed = attackSpeed;
        this.swordProperties = properties;
        return this;
    }

    public ToolsetConfig pickaxe(int attackDamage, float attackSpeed, Consumer<Item.Properties> properties) {
        this.pickaxeAttackDamage = attackDamage;
        this.pickaxeAttackSpeed = attackSpeed;
        this.pickaxeProperties = properties;
        return this;
    }

    public ToolsetConfig axe(int attackDamage, float attackSpeed, Consumer<Item.Properties> properties) {
        this.axeAttackDamage = attackDamage;
        this.axeAttackSpeed = attackSpeed;
        this.axeProperties = properties;
        return this;
    }

    public ToolsetConfig shovel(int attackDamage, float attackSpeed, Consumer<Item.Properties> properties) {
        this.shovelAttackDamage = attackDamage;
        this.shovelAttackSpeed = attackSpeed;
        this.shovelProperties = properties;
        return this;
    }

    public ToolsetConfig hoe(int attackDamage, float attackSpeed, Consumer<Item.Properties> properties) {
        this.hoeAttackDamage = attackDamage;
        this.hoeAttackSpeed = attackSpeed;
        this.hoeProperties = properties;
        return this;
    }
}
