/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.compat.jei;

import java.util.Set;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.compat.jei.brewing.BrewingJeiProvider;
import software.bluelib.internal.BlueResource;

@ApiStatus.Internal
@JeiPlugin
public class BlueJeiPlugin implements IModPlugin {

	@NotNull
	private static final Set<BlueJeiProvider> jeiProviders = Set.of(
			new BrewingJeiProvider());

	@Nullable
	public static IJeiRuntime jeiRuntime = null;
	@NotNull
	public static final ResourceLocation ID = BlueResource.resource("jei_plugin");

	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerCategories(@NotNull IRecipeCategoryRegistration pRegistration) {
		for (BlueJeiProvider provider : jeiProviders) {
			provider.registerCategory(pRegistration);
		}
	}

	@Override
	public void registerRecipes(@NotNull IRecipeRegistration pRegistration) {
		for (BlueJeiProvider provider : jeiProviders) {
			provider.registerRecipes(pRegistration);
		}
	}

	@Override
	public void onRuntimeAvailable(@NotNull IJeiRuntime pJeiRuntime) {
		jeiRuntime = pJeiRuntime;
	}
}
