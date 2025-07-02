/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import java.util.List;
import net.neoforged.fml.ModLoader;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;

public class BlueLibEventProxy implements IEventProxy {

	@Override
	public void onModLoaded(@NotNull ModMeta pModData) {
		ModLoader.postEvent(new ModLoadedEvent(pModData));
	}

	@Override
	public void onAllModsLoaded(@NotNull List<ModMeta> pModData) {
		ModLoader.postEvent(new AllModsLoadedEvent(pModData));
	}

	@Override
	public @NotNull Boolean variantLoadedPre(@NotNull String pEntityName, @NotNull String pVariant) {
		VariantLoadedEvent.Pre event = new VariantLoadedEvent.Pre(pEntityName, pVariant);
		return ModLoader.postEventWithReturn(event).isCanceled();
	}

	@Override
	public void variantLoadedPost(@NotNull String pEntityName, @NotNull String pVariant) {
		ModLoader.postEvent(new VariantLoadedEvent.Post(pEntityName, pVariant));
	}

	@Override
	public @NotNull Boolean allVariantsLoadedPre(@NotNull String pEntityName) {
		AllVariantsLoadedEvent.Pre event = new AllVariantsLoadedEvent.Pre(pEntityName);
		return ModLoader.postEventWithReturn(event).isCanceled();
	}

	@Override
	public void allVariantsLoadedPost(@NotNull String pEntityName) {
		ModLoader.postEvent(new AllVariantsLoadedEvent.Post(pEntityName));
	}
}
