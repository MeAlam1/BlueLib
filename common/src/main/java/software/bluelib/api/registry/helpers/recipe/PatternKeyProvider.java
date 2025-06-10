package software.bluelib.api.registry.helpers.recipe;

import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

// PatternKeyProvider.java
public interface PatternKeyProvider {
    List<String> getPattern();
    Map<Character, Ingredient> getKey();
}
