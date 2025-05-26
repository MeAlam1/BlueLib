/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.common.NeoForge;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.event.GeoRenderEvent;
import software.bluelib.loader.renderer.*;

import java.util.List;

// TODO: Take a Look into Events, maybe post through NeoForge.EVENT_BUS and Check the Cancelling!!!!
public class BlueLibEventProxy implements IEventProxy {

	@Override
	public void onModLoaded(ModMeta pModData) {
		ModLoader.postEvent(new ModLoadedEvent(pModData));
	}

	@Override
	public void onAllModsLoaded(List<ModMeta> pModData) {
		ModLoader.postEvent(new AllModsLoadedEvent(pModData));
	}

	@Override
	public boolean variantLoadedPre(String pEntityName, String pVariant) {
		VariantLoadedEvent.Pre event = new VariantLoadedEvent.Pre(pEntityName, pVariant);
		return ModLoader.postEventWithReturn(event).isCanceled();
	}

	@Override
	public void variantLoadedPost(String pEntityName, String pVariant) {
		ModLoader.postEvent(new VariantLoadedEvent.Post(pEntityName, pVariant));
	}

	@Override
	public boolean allVariantsLoadedPre(String pEntityName) {
		AllVariantsLoadedEvent.Pre event = new AllVariantsLoadedEvent.Pre(pEntityName);
		return ModLoader.postEventWithReturn(event).isCanceled();
	}

	@Override
	public void allVariantsLoadedPost(String pEntityName) {
		ModLoader.postEvent(new AllVariantsLoadedEvent.Post(pEntityName));
	}

	@Override
	public void fireCompileBlockRenderLayers(GeoBlockRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Block.CompileRenderLayers(pRenderer));
	}
	
	@Override
	public boolean fireBlockPreRender(GeoBlockRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Block.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}
	
	@Override
	public void fireBlockPostRender(GeoBlockRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Block.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}
	
	@Override
	public void fireCompileArmorRenderLayers(GeoArmorRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Armor.CompileRenderLayers(pRenderer));
	}
	
	@Override
	public boolean fireArmorPreRender(GeoArmorRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Armor.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}
	
	@Override
	public void fireArmorPostRender(GeoArmorRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Armor.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}
	
	@Override
	public void fireCompileEntityRenderLayers(GeoEntityRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Entity.CompileRenderLayers(pRenderer));
	}
	
	@Override
	public boolean fireEntityPreRender(GeoEntityRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Entity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}
	
	@Override
	public void fireEntityPostRender(GeoEntityRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Entity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}
	
	@Override
	public void fireCompileReplacedEntityRenderLayers(GeoReplacedEntityRenderer<?, ?> pRenderer) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.CompileRenderLayers(pRenderer));
	}
	
	@Override
	public boolean fireReplacedEntityPreRender(GeoReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}
	
	@Override
	public void fireReplacedEntityPostRender(GeoReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}
	
	@Override
	public void fireCompileItemRenderLayers(GeoItemRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Item.CompileRenderLayers(pRenderer));
	}
	
	@Override
	public boolean fireItemPreRender(GeoItemRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Item.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}
	
	@Override
	public void fireItemPostRender(GeoItemRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Item.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}
	
	@Override
	public void fireCompileObjectRenderLayers(GeoObjectRenderer<?> pRenderer) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Object.CompileRenderLayers(pRenderer));
	}
	
	@Override
	public boolean fireObjectPreRender(GeoObjectRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Object.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)).isCanceled();
	}
	
	@Override
	public void fireObjectPostRender(GeoObjectRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		NeoForge.EVENT_BUS.post(new GeoRenderEvent.Object.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
	}
}
