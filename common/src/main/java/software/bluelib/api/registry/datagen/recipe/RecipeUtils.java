package software.bluelib.api.registry.datagen.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.lang.reflect.Field;

public class RecipeUtils {

    public static JsonObject serializeIngredient(Ingredient ingredient) {
        JsonElement json = Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient).result().orElseThrow();
        return json.getAsJsonObject();
    }

    public static JsonObject serializeResult(ItemStack stack) {
        JsonObject result = new JsonObject();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        result.addProperty("id", id.toString());
        if (stack.getCount() > 1) {
            result.addProperty("count", stack.getCount());
        }
        return result;
    }

    public static ItemStack getSmithingRecipeResult(Recipe<?> recipe) {
        try {
            Field resultField = recipe.getClass().getDeclaredField("result");
            resultField.setAccessible(true);
            Object value = resultField.get(recipe);
            if (value instanceof ItemStack stack) {
                return stack;
            }
        } catch (Exception ignored) {}
        return ItemStack.EMPTY;
    }
}
