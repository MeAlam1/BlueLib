/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.data.recipes.RecipeOutput;
import software.bluelib.BlueLibConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RecipeGenerator {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping() // Prevent escaping of =, <, >, etc.
            .create();

    public static void generateRecipe(String modId, String name, BiConsumer<RecipeOutput, Supplier<JsonElement>> recipeConsumer) {
        Path recipePath = Path.of(BlueLibConstants.PlatformHelper.PLATFORM.getDataDir(true) + "/recipes/" + name + ".json");

        try {
            if (Files.exists(recipePath)) {
                System.out.println("Recipe for '" + name + "' already exists at: " + recipePath + ". Skipping creation.");
                return;
            }

            JsonElement recipeJson = BlueLibConstants.PlatformHelper.PLATFORM.generateRecipeJson(modId, name, recipeConsumer);

            if (recipeJson == null) {
                System.err.println("Failed to generate recipe JSON for '" + name + "'. Skipping file creation.");
                return;
            }

            Files.createDirectories(recipePath.getParent());
            Files.write(recipePath, GSON.toJson(recipeJson).getBytes(), StandardOpenOption.CREATE_NEW);
            System.out.println("Recipe for '" + name + "' created at: " + recipePath);

        } catch (IOException e) {
            System.err.println("Failed [ERROR]: Failed to create recipe for '" + name + "' at " + recipePath + ": " + e.getMessage());
        }
    }
}