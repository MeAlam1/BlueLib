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
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.loader.event.BlueRenderEvent;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.renderer.*;

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

	@Override
	public void fireCompileBlockRenderLayers(@NotNull BlueBlockRenderer<?> pRenderer) {
		BlueRenderEvent.Block.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Block.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireBlockPreRender(@NotNull BlueBlockRenderer<?> pRenderer, IRenderContext<?> pContext) {
		return BlueRenderEvent.Block.Pre.EVENT.invoker().handle(new BlueRenderEvent.Block.Pre(pRenderer, pContext));
	}

	@Override
	public void fireBlockPostRender(@NotNull BlueBlockRenderer<?> pRenderer, IRenderContext<?> pContext) {
		BlueRenderEvent.Block.Post.EVENT.invoker().handle(new BlueRenderEvent.Block.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileArmorRenderLayers(@NotNull BlueArmorRenderer<?> pRenderer) {
		BlueRenderEvent.Armor.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Armor.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireArmorPreRender(@NotNull BlueArmorRenderer<?> pRenderer, IRenderContext<?> pContext) {
		return BlueRenderEvent.Armor.Pre.EVENT.invoker().handle(new BlueRenderEvent.Armor.Pre(pRenderer, pContext));
	}

	@Override
	public void fireArmorPostRender(@NotNull BlueArmorRenderer<?> pRenderer, IRenderContext<?> pContext) {
		BlueRenderEvent.Armor.Post.EVENT.invoker().handle(new BlueRenderEvent.Armor.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileEntityRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer) {
		BlueRenderEvent.Entity.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Entity.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireEntityPreRender(@NotNull BlueEntityRenderer<?> pRenderer, IRenderContext<?> pContext) {
		return BlueRenderEvent.Entity.Pre.EVENT.invoker().handle(new BlueRenderEvent.Entity.Pre(pRenderer, pContext));
	}

	@Override
	public void fireEntityPostRender(@NotNull BlueEntityRenderer<?> pRenderer, IRenderContext<?> pContext) {
		BlueRenderEvent.Entity.Post.EVENT.invoker().handle(new BlueRenderEvent.Entity.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileReplacedEntityRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer) {
		BlueRenderEvent.ReplacedEntity.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.ReplacedEntity.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireReplacedEntityPreRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, IRenderContext<?> pContext) {
		return BlueRenderEvent.ReplacedEntity.Pre.EVENT.invoker().handle(new BlueRenderEvent.ReplacedEntity.Pre(pRenderer, pContext));
	}

	@Override
	public void fireReplacedEntityPostRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, IRenderContext<?> pContext) {
		BlueRenderEvent.ReplacedEntity.Post.EVENT.invoker().handle(new BlueRenderEvent.ReplacedEntity.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileItemRenderLayers(@NotNull BlueItemRenderer<?> pRenderer) {
		BlueRenderEvent.Item.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Item.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireItemPreRender(@NotNull BlueItemRenderer<?> pRenderer, IRenderContext<?> pContext) {
		return BlueRenderEvent.Item.Pre.EVENT.invoker().handle(new BlueRenderEvent.Item.Pre(pRenderer, pContext));
	}

	@Override
	public void fireItemPostRender(@NotNull BlueItemRenderer<?> pRenderer, IRenderContext<?> pContext) {
		BlueRenderEvent.Item.Post.EVENT.invoker().handle(new BlueRenderEvent.Item.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileObjectRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer) {
		BlueRenderEvent.Object.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Object.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireObjectPreRender(@NotNull BlueObjectRenderer<?> pRenderer, IRenderContext<?> pContext) {
		return BlueRenderEvent.Object.Pre.EVENT.invoker().handle(new BlueRenderEvent.Object.Pre(pRenderer, pContext));
	}

	@Override
	public void fireObjectPostRender(@NotNull BlueObjectRenderer<?> pRenderer, IRenderContext<?> pContext) {
		BlueRenderEvent.Object.Post.EVENT.invoker().handle(new BlueRenderEvent.Object.Post(pRenderer, pContext));
	}
}
