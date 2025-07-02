/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.recipe.brewing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bluelib.internal.registry.BlueRecipeSerializerRegistry;
import software.bluelib.internal.registry.BlueRecipeTypeRegistry;

public class BrewingRecipe implements Recipe<BrewingInput> {

	@NotNull
	private final String groupName;
	@NotNull
	private final Ingredient input;
	@NotNull
	private final Ingredient bottle;
	@NotNull
	private final ItemStack result;

	public BrewingRecipe(@NotNull String pGroupName, @NotNull Ingredient pInput, @NotNull Ingredient pBottle, @NotNull ItemStack pResult) {
		this.groupName = pGroupName;
		this.input = pInput;
		this.bottle = pBottle;
		this.result = pResult;
	}

	@Override
	public boolean matches(@NotNull BrewingInput pInputData, @NotNull Level pLevel) {
		boolean ingredientMatches = this.input.test(pInputData.getIngredient());
		List<ItemStack> bottles = pInputData.getBottles();
		boolean validBottles = bottles.stream()
				.filter(stack -> !stack.isEmpty())
				.allMatch(this.bottle);

		return ingredientMatches && validBottles;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull BrewingInput pInput, HolderLookup.@NotNull Provider pRegistries) {
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	@Override
	public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider pRegistries) {
		return result.copy();
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return BlueRecipeSerializerRegistry.BREWING.get();
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return BlueRecipeTypeRegistry.BREWING.get();
	}

	@NotNull
	public String getGroupName() {
		return groupName;
	}

	@NotNull
	public Ingredient getInputIngredient() {
		return input;
	}

	@NotNull
	public Ingredient getBottleIngredient() {
		return bottle;
	}

	@NotNull
	public ItemStack getResult() {
		return result;
	}

	public static boolean isBottle(@NotNull ItemStack pItemStack, @NotNull RecipeManager pRecipeManager) {
		return pRecipeManager.getAllRecipesFor(BlueRecipeTypeRegistry.BREWING.get()).stream()
				.anyMatch(recipe -> recipe.value().getBottleIngredient().test(pItemStack));
	}

	public static boolean isInput(@NotNull ItemStack pItemStack, @NotNull RecipeManager pRecipeManager) {
		return pRecipeManager.getAllRecipesFor(BlueRecipeTypeRegistry.BREWING.get()).stream()
				.anyMatch(recipe -> recipe.value().getInputIngredient().test(pItemStack));
	}

	public static class Serializer implements RecipeSerializer<BrewingRecipe> {

		@NotNull
		public static final MapCodec<BrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(BrewingRecipe::getGroupName),
				Ingredient.CODEC.fieldOf("input").forGetter(BrewingRecipe::getInputIngredient),
				Ingredient.CODEC.fieldOf("bottle").forGetter(BrewingRecipe::getBottleIngredient),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(BrewingRecipe::getResult)).apply(instance, BrewingRecipe::new));

		@NotNull
		public static final StreamCodec<RegistryFriendlyByteBuf, BrewingRecipe> STREAM_CODEC = StreamCodec.of(
				Serializer::toNetwork,
				Serializer::fromNetwork);

		@NotNull
		private static BrewingRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf pBuffer) {
			String group = pBuffer.readUtf(32767);
			Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
			Ingredient bottle = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
			ItemStack result = ItemStack.STREAM_CODEC.decode(pBuffer);
			return new BrewingRecipe(group, input, bottle, result);
		}

		private static void toNetwork(@NotNull RegistryFriendlyByteBuf pBuffer, @NotNull BrewingRecipe pRecipe) {
			pBuffer.writeUtf(pRecipe.getGroupName());
			Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.getInputIngredient());
			Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.getBottleIngredient());
			ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.getResult());
		}

		@Override
		public @NotNull MapCodec<BrewingRecipe> codec() {
			return CODEC;
		}

		@Override
		public @NotNull StreamCodec<RegistryFriendlyByteBuf, BrewingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
