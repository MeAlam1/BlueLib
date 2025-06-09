/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NeoRecipeGenerator {
    public static JsonElement generateRecipeJson(BiConsumer<RecipeOutput, Supplier<JsonElement>> recipeConsumer) {
        final JsonElement[] capturedJson = new JsonElement[1];

        RecipeOutput tempOutput = new RecipeOutput() {
            @Override
            public void accept(ResourceLocation resourceLocation, Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                if (capturedJson[0] == null) {
                    System.err.println("No JSON provided for recipe: " + resourceLocation);
                    capturedJson[0] = new JsonObject();
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
}