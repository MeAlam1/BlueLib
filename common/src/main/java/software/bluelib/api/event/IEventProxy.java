/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.renderer.*;

public interface IEventProxy {

	// Mod Events

	void onModLoaded(@NotNull ModMeta pModData);

	void onAllModsLoaded(@NotNull List<ModMeta> pModData);

	// Variant Events

	@NotNull
	Boolean variantLoadedPre(@NotNull String pEntityName, @NotNull String pVariant);

	void variantLoadedPost(@NotNull String pEntityName, @NotNull String pVariant);

	@NotNull
	Boolean allVariantsLoadedPre(@NotNull String pEntityName);

	void allVariantsLoadedPost(@NotNull String pEntityName);

	// Render Events

	// Block
	void fireCompileBlockRenderLayers(@NotNull BlueBlockRenderer<?> pRenderer);

	@NotNull
	Boolean fireBlockPreRender(@NotNull BlueBlockRenderer<?> pRenderer, IRenderContext<?> pContext);

	void fireBlockPostRender(@NotNull BlueBlockRenderer<?> pRenderer, IRenderContext<?> pContext);

	// Armor

	void fireCompileArmorRenderLayers(@NotNull BlueArmorRenderer<?> pRenderer);

	@NotNull
	Boolean fireArmorPreRender(@NotNull BlueArmorRenderer<?> pRenderer, IRenderContext<?> pContext);

	void fireArmorPostRender(@NotNull BlueArmorRenderer<?> pRenderer, IRenderContext<?> pContext);

	// Entity

	void fireCompileEntityRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer);

	@NotNull
	Boolean fireEntityPreRender(@NotNull BlueEntityRenderer<?> pRenderer, IRenderContext<?> pContext);

	void fireEntityPostRender(@NotNull BlueEntityRenderer<?> pRenderer, IRenderContext<?> pContext);

	void fireCompileReplacedEntityRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer);

	@NotNull
	Boolean fireReplacedEntityPreRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, IRenderContext<?> pContext);

	void fireReplacedEntityPostRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, IRenderContext<?> pContext);

	// Item

	void fireCompileItemRenderLayers(@NotNull BlueItemRenderer<?> pRenderer);

	@NotNull
	Boolean fireItemPreRender(@NotNull BlueItemRenderer<?> pRenderer, IRenderContext<?> pContext);

	void fireItemPostRender(@NotNull BlueItemRenderer<?> pRenderer, IRenderContext<?> pContext);

	// Object

	void fireCompileObjectRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer);

	@NotNull
	Boolean fireObjectPreRender(@NotNull BlueObjectRenderer<?> pRenderer, IRenderContext<?> pContext);

	void fireObjectPostRender(@NotNull BlueObjectRenderer<?> pRenderer, IRenderContext<?> pContext);
}
