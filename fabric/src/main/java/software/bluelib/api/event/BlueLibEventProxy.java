/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;

import java.util.List;

public class BlueLibEventProxy implements IEventProxy {

	@Override
	public void onModLoaded(@NotNull ModMeta pModData) {
		ModLoadedEvent.EVENT.invoker().onModLoaded(pModData);
	}

	@Override
	public void onAllModsLoaded(@NotNull List<ModMeta> pModData) {
		AllModsLoadedEvent.EVENT.invoker().onAllModsLoaded(pModData);
	}

	@Override
	public @NotNull Boolean variantLoadedPre(@NotNull String pEntityName, @NotNull String pVariant) {
		return !VariantLoadedEvent.ALLOW_VARIANT_TO_LOAD.invoker().allowVariantToLoad(pEntityName, pVariant);
	}

	@Override
	public void variantLoadedPost(@NotNull String pEntityName, @NotNull String pVariant) {
		VariantLoadedEvent.POST.invoker().onVariantLoaded(pEntityName, pVariant);
	}

	@Override
	public @NotNull Boolean allVariantsLoadedPre(@NotNull String pEntityName) {
		return !AllVariantsLoadedEvent.ALLOW_ALL_VARIANTS_TO_LOAD.invoker().allowAllVariantsToLoad(pEntityName);
	}

	@Override
	public void allVariantsLoadedPost(@NotNull String pEntityName) {
		AllVariantsLoadedEvent.POST.invoker().onAllVariantsLoaded(pEntityName);
	}
}
