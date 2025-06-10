/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.registry.helpers.recipe.PatternKeyProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NeoRecipeGenerator {
    public static JsonElement generateRecipeJson(BiConsumer<RecipeOutput, Supplier<JsonElement>> recipeConsumer) {
        final JsonElement[] capturedJson = new JsonElement[1];

        RecipeOutput tempOutput = new RecipeOutput() {
            @Override
            public void accept(@NotNull ResourceLocation pPath, @NotNull Recipe<?> pRecipe, @Nullable AdvancementHolder pAdvancement, ICondition @NotNull ... iConditions) {
                if (pRecipe instanceof ShapedRecipe shaped) {
                    JsonObject json = new JsonObject();
                    json.addProperty("type", "minecraft:crafting_shaped");
                    json.addProperty("category", "misc");

                    // Try to get original pattern/key if available
                    java.util.List<String> patternList = null;
                    java.util.Map<Character, Ingredient> keyMap = null;
                    if (pRecipe instanceof PatternKeyProvider provider) {
                        patternList = provider.getPattern();
                        keyMap = provider.getKey();
                    }

                    JsonArray pattern = new JsonArray();
                    JsonObject key = new JsonObject();

                    if (patternList != null && keyMap != null) {
                        // Use captured pattern/key
                        for (String row : patternList) pattern.add(row);
                        for (var entry : keyMap.entrySet())
                            key.add(String.valueOf(entry.getKey()), serializeIngredient(entry.getValue()));
                    } else {
                        // Fallback: reconstruct pattern/key with generic letters
                        int width = shaped.getWidth();
                        int height = shaped.getHeight();
                        char symbol = 'A';
                        char[][] symbols = new char[height][width];
                        Map<Ingredient, Character> ingredientSymbols = new HashMap<>();
                        for (int i = 0; i < height; i++) {
                            StringBuilder row = new StringBuilder();
                            for (int j = 0; j < width; j++) {
                                Ingredient ingredient = shaped.getIngredients().get(i * width + j);
                                if (ingredient.isEmpty()) {
                                    row.append(' ');
                                    symbols[i][j] = ' ';
                                } else {
                                    ingredientSymbols.putIfAbsent(ingredient, symbol);
                                    char c = ingredientSymbols.get(ingredient);
                                    row.append(c);
                                    symbols[i][j] = c;
                                    symbol++;
                                }
                            }
                            pattern.add(row.toString());
                        }
                        for (var entry : ingredientSymbols.entrySet())
                            key.add(String.valueOf(entry.getValue()), serializeIngredient(entry.getKey()));
                    }

                    json.add("pattern", pattern);
                    json.add("key", key);
                    json.add("result", serializeResult(shaped.getResultItem(null)));
                    capturedJson[0] = json;
                } else if (pRecipe instanceof ShapelessRecipe shapeless) {
                    JsonObject json = new JsonObject();
                    json.addProperty("type", "minecraft:crafting_shapeless");
                    json.addProperty("category", "misc");
                    JsonArray ingredients = new JsonArray();
                    for (Ingredient ing : shapeless.getIngredients()) {
                        ingredients.add(serializeIngredient(ing));
                    }
                    json.add("ingredients", ingredients);
                    json.add("result", serializeResult(shapeless.getResultItem(null)));
                    capturedJson[0] = json;
                } else {
                    JsonObject json = new JsonObject();
                    json.addProperty("type", pRecipe.getSerializer().toString());
                    capturedJson[0] = json;
                }
            }
            @Override
            public net.minecraft.advancements.Advancement.@NotNull Builder advancement() {
                return net.minecraft.advancements.Advancement.Builder.advancement();
            }
        };

        recipeConsumer.accept(tempOutput, () -> capturedJson[0]);
        return capturedJson[0] != null ? capturedJson[0] : new JsonObject();
    }

    private static JsonObject serializeIngredient(Ingredient ingredient) {
        JsonElement json = Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient).result().orElseThrow();
        return json.getAsJsonObject();
    }

    private static JsonObject serializeResult(ItemStack stack) {
        JsonObject result = new JsonObject();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        result.addProperty("item", id.toString());
        if (stack.getCount() > 1) {
            result.addProperty("count", stack.getCount());
        }
        return result;
    }
}