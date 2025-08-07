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
import software.bluelib.loader.renderer.BlueObjectRenderer;
import software.bluelib.loader.renderer.armor.BlueArmorRenderer;
import software.bluelib.loader.renderer.block.BlueBlockRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.loader.renderer.entity.BlueEntityRenderer;
import software.bluelib.loader.renderer.entity.BlueReplacedEntityRenderer;
import software.bluelib.loader.renderer.item.BlueItemRenderer;

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
	Boolean fireBlockPreRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	void fireBlockPostRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	// Armor

	void fireCompileArmorRenderLayers(@NotNull BlueArmorRenderer<?> pRenderer);

	@NotNull
	Boolean fireArmorPreRender(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	void fireArmorPostRender(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	// Entity

	void fireCompileEntityRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer);

	@NotNull
	Boolean fireEntityPreRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	void fireEntityPostRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	void fireCompileReplacedEntityRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer);

	@NotNull
	Boolean fireReplacedEntityPreRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull IRenderContext<?> pContext);

	void fireReplacedEntityPostRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull IRenderContext<?> pContext);

	// Item

	void fireCompileItemRenderLayers(@NotNull BlueItemRenderer<?> pRenderer);

	@NotNull
	Boolean fireItemPreRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	void fireItemPostRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	// Object

	void fireCompileObjectRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer);

	@NotNull
	Boolean fireObjectPreRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);

	void fireObjectPostRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext);
}
