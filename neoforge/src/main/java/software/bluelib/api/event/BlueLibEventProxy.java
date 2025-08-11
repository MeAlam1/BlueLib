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
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.loader.renderer.BlueObjectRenderer;
import software.bluelib.loader.renderer.armor.BlueArmorRenderer;
import software.bluelib.loader.renderer.block.BlueBlockRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.loader.renderer.entity.BlueEntityRenderer;
import software.bluelib.loader.renderer.entity.BlueReplacedEntityRenderer;
import software.bluelib.loader.renderer.item.BlueItemRenderer;

// TODO: Take a Look into Events, maybe post through NeoForge.EVENT_BUS and Check the Cancelling!!!!
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

	@Override
	public void fireCompileBlockRenderLayers(@NotNull BlueBlockRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Block.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireBlockPreRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Block.Pre(pRenderer, pContext)).isCanceled();
	}

	@Override
	public void fireBlockPostRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Block.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileArmorRenderLayers(@NotNull BlueArmorRenderer<?, ?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Armor.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireArmorPreRender(@NotNull BlueArmorRenderer<?, ?> pRenderer, @NotNull IRenderContext<?> pContext) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Armor.Pre(pRenderer, pContext)).isCanceled();
	}

	@Override
	public void fireArmorPostRender(@NotNull BlueArmorRenderer<?, ?> pRenderer, @NotNull IRenderContext<?> pContext) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Armor.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileEntityRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Entity.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireEntityPreRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Entity.Pre(pRenderer, pContext)).isCanceled();
	}

	@Override
	public void fireEntityPostRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Entity.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileReplacedEntityRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.ReplacedEntity.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireReplacedEntityPreRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull IRenderContext<?> pContext) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.ReplacedEntity.Pre(pRenderer, pContext)).isCanceled();
	}

	@Override
	public void fireReplacedEntityPostRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull IRenderContext<?> pContext) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.ReplacedEntity.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileItemRenderLayers(@NotNull BlueItemRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Item.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireItemPreRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Item.Pre(pRenderer, pContext)).isCanceled();
	}

	@Override
	public void fireItemPostRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Item.Post(pRenderer, pContext));
	}

	@Override
	public void fireCompileObjectRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Object.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireObjectPreRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Object.Pre(pRenderer, pContext)).isCanceled();
	}

	@Override
	public void fireObjectPostRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull IRenderContext<?> pContext) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Object.Post(pRenderer, pContext));
	}
}
