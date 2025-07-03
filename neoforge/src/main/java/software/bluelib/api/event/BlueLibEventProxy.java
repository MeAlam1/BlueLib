/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.loader.event.BlueRenderEvent;
import software.bluelib.oldLoader.renderer.*;

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
	public @NotNull Boolean fireBlockPreRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Block.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}

	@Override
	public void fireBlockPostRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Block.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}

	@Override
	public void fireCompileArmorRenderLayers(@NotNull BlueArmorRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Armor.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireArmorPreRender(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Armor.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}

	@Override
	public void fireArmorPostRender(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Armor.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}

	@Override
	public void fireCompileEntityRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Entity.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireEntityPreRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Entity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}

	@Override
	public void fireEntityPostRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Entity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}

	@Override
	public void fireCompileReplacedEntityRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.ReplacedEntity.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireReplacedEntityPreRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.ReplacedEntity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}

	@Override
	public void fireReplacedEntityPostRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.ReplacedEntity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}

	@Override
	public void fireCompileItemRenderLayers(@NotNull BlueItemRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Item.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireItemPreRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Item.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}

	@Override
	public void fireItemPostRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Item.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}

	@Override
	public void fireCompileObjectRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Object.CompileRenderLayers(pRenderer));
	}

	@Override
	public @NotNull Boolean fireObjectPreRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new BlueRenderEvent.Object.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}

	@Override
	public void fireObjectPostRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
		NeoForge.EVENT_BUS.post(new BlueRenderEvent.Object.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}
}
