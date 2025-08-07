/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry;

import static software.bluelib.api.registry.datagen.recipe.RecipeUtils.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricRecipeGenerator {

	public static JsonElement generateRecipeJson(BiConsumer<RecipeOutput, Supplier<JsonElement>> recipeConsumer) {
		final JsonElement[] capturedJson = new JsonElement[1];

		RecipeOutput tempOutput = new RecipeOutput() {

			@Override
			public void accept(ResourceLocation pPath, Recipe<?> pRecipe, @Nullable AdvancementHolder pAdvancement) {
				switch (pRecipe) {
					case ShapedRecipe shaped -> {
						JsonObject json = new JsonObject();
						json.addProperty("type", "minecraft:crafting_shaped");
						json.addProperty("category", "misc");
						List<String> patternList = null;
						Map<Character, Ingredient> keyMap;
						JsonArray pattern = new JsonArray();
						JsonObject key = new JsonObject();

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

						json.add("pattern", pattern);
						json.add("key", key);
						json.add("result", serializeResult(shaped.getResultItem(null)));
						capturedJson[0] = json;
					}
					case ShapelessRecipe shapeless -> {
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
					}
					case AbstractCookingRecipe cooking -> {
						JsonObject json = new JsonObject();
						String type;
						if (cooking.getSerializer().equals(RecipeSerializer.SMELTING_RECIPE)) {
							type = "minecraft:smelting";
						} else if (cooking.getSerializer().equals(RecipeSerializer.BLASTING_RECIPE)) {
							type = "minecraft:blasting";
						} else if (cooking.getSerializer().equals(RecipeSerializer.SMOKING_RECIPE)) {
							type = "minecraft:smoking";
						} else if (cooking.getSerializer().equals(RecipeSerializer.CAMPFIRE_COOKING_RECIPE)) {
							type = "minecraft:campfire_cooking";
						} else {
							type = cooking.getSerializer().toString();
						}
						json.addProperty("type", type);
						json.addProperty("category", "misc");
						JsonArray ingredients = new JsonArray();
						ingredients.add(serializeIngredient(cooking.getIngredients().get(0)));
						json.add("ingredient", ingredients.get(0));
						json.add("result", serializeResult(cooking.getResultItem(null)));
						json.addProperty("experience", cooking.getExperience());
						json.addProperty("cookingtime", cooking.getCookingTime());
						capturedJson[0] = json;
					}
					case SmithingTransformRecipe smithingTransform -> {
						JsonObject json = new JsonObject();
						json.addProperty("type", "minecraft:smithing_transform");
						json.add("template", serializeIngredient(smithingTransform.template));
						json.add("base", serializeIngredient(smithingTransform.base));
						json.add("addition", serializeIngredient(smithingTransform.addition));
						json.add("result", serializeResult(smithingTransform.getResultItem(null)));
						capturedJson[0] = json;
					}
					case SmithingTrimRecipe smithingTrim -> {
						JsonObject json = new JsonObject();
						json.addProperty("type", "minecraft:smithing_trim");
						json.add("template", serializeIngredient(smithingTrim.template));
						json.add("base", serializeIngredient(smithingTrim.base));
						json.add("addition", serializeIngredient(smithingTrim.addition));
						json.add("result", serializeResult(getSmithingRecipeResult(smithingTrim)));
						capturedJson[0] = json;
					}
					default -> {
						JsonObject json = new JsonObject();
						json.addProperty("type", pRecipe.getSerializer().toString());
						capturedJson[0] = json;
					}
				}
			}

			@Override
			public Advancement.@NotNull Builder advancement() {
				return Advancement.Builder.advancement();
			}
		};

		recipeConsumer.accept(tempOutput, () -> capturedJson[0]);
		return capturedJson[0] != null ? capturedJson[0] : new JsonObject();
	}
}
