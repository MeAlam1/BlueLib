/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.compat.jei;

import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

public interface BlueJeiProvider {
	void registerCategory(IRecipeCategoryRegistration pRegistration);
	void registerRecipes(IRecipeRegistration pRegistration);
}
