package software.bluelib.api.registry.helpers.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapturingShapedRecipeBuilder extends ShapedRecipeBuilder {
    private final List<String> pattern = new ArrayList<>();
    private final Map<Character, Ingredient> key = new HashMap<>();
    // Static map to store by recipe id
    public static final Map<String, PatternKey> PATTERN_KEY_MAP = new HashMap<>();

    public CapturingShapedRecipeBuilder(RecipeCategory category, Item result) {
        super(category, result, 1);
    }

    @Override
    public ShapedRecipeBuilder pattern(String patternLine) {
        pattern.add(patternLine);
        return super.pattern(patternLine);
    }

    @Override
    public ShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
        key.put(symbol, ingredient);
        return super.define(symbol, ingredient);
    }

    @Override
    public void save(RecipeOutput output) {
        save(output, BuiltInRegistries.ITEM.getKey(getResult()).getPath());
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        // Store pattern/key for this recipe id
        PATTERN_KEY_MAP.put(recipeId, new PatternKey(new ArrayList<>(pattern), new HashMap<>(key)));
        super.save(output, recipeId);
    }

    public static class PatternKey {
        public final List<String> pattern;
        public final Map<Character, Ingredient> key;
        public PatternKey(List<String> pattern, Map<Character, Ingredient> key) {
            this.pattern = pattern;
            this.key = key;
        }
    }
}
