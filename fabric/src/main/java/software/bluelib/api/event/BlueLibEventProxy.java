/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.event.GeoRenderEvent;
import software.bluelib.loader.renderer.*;

public class BlueLibEventProxy implements IEventProxy {

    @Override
    public void onModLoaded(ModMeta pModData) {
        ModLoadedEvent.EVENT.invoker().onModLoaded(pModData);
    }

    @Override
    public void onAllModsLoaded(List<ModMeta> pModData) {
        AllModsLoadedEvent.EVENT.invoker().onAllModsLoaded(pModData);
    }

    @Override
    public boolean variantLoadedPre(String pEntityName, String pVariant) {
        return !VariantLoadedEvent.ALLOW_VARIANT_TO_LOAD.invoker().allowVariantToLoad(pEntityName, pVariant);
    }

    @Override
    public void variantLoadedPost(String pEntityName, String pVariant) {
        VariantLoadedEvent.POST.invoker().onVariantLoaded(pEntityName, pVariant);
    }

    @Override
    public boolean allVariantsLoadedPre(String pEntityName) {
        return !AllVariantsLoadedEvent.ALLOW_ALL_VARIANTS_TO_LOAD.invoker().allowAllVariantsToLoad(pEntityName);
    }

    @Override
    public void allVariantsLoadedPost(String pEntityName) {
        AllVariantsLoadedEvent.POST.invoker().onAllVariantsLoaded(pEntityName);
    }
    
    @Override
    public void fireCompileBlockRenderLayers(GeoBlockRenderer<?> pRenderer) {
        GeoRenderEvent.Block.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Block.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireBlockPreRender(GeoBlockRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return GeoRenderEvent.Block.Pre.EVENT.invoker().handle(new GeoRenderEvent.Block.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireBlockPostRender(GeoBlockRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        GeoRenderEvent.Block.Post.EVENT.invoker().handle(new GeoRenderEvent.Block.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileArmorRenderLayers(GeoArmorRenderer<?> pRenderer) {
        GeoRenderEvent.Armor.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Armor.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireArmorPreRender(GeoArmorRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return GeoRenderEvent.Armor.Pre.EVENT.invoker().handle(new GeoRenderEvent.Armor.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireArmorPostRender(GeoArmorRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        GeoRenderEvent.Armor.Post.EVENT.invoker().handle(new GeoRenderEvent.Armor.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileEntityRenderLayers(GeoEntityRenderer<?> pRenderer) {
        GeoRenderEvent.Entity.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Entity.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireEntityPreRender(GeoEntityRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return GeoRenderEvent.Entity.Pre.EVENT.invoker().handle(new GeoRenderEvent.Entity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireEntityPostRender(GeoEntityRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        GeoRenderEvent.Entity.Post.EVENT.invoker().handle(new GeoRenderEvent.Entity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileReplacedEntityRenderLayers(GeoReplacedEntityRenderer<?, ?> pRenderer) {
        GeoRenderEvent.ReplacedEntity.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.ReplacedEntity.CompileRenderLayers(pRenderer));
    }

    @Override
    public boolean fireReplacedEntityPreRender(GeoReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return GeoRenderEvent.ReplacedEntity.Pre.EVENT.invoker().handle(new GeoRenderEvent.ReplacedEntity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireReplacedEntityPostRender(GeoReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        GeoRenderEvent.ReplacedEntity.Post.EVENT.invoker().handle(new GeoRenderEvent.ReplacedEntity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileItemRenderLayers(GeoItemRenderer<?> pRenderer) {
        GeoRenderEvent.Item.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Item.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireItemPreRender(GeoItemRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return GeoRenderEvent.Item.Pre.EVENT.invoker().handle(new GeoRenderEvent.Item.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireItemPostRender(GeoItemRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        GeoRenderEvent.Item.Post.EVENT.invoker().handle(new GeoRenderEvent.Item.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileObjectRenderLayers(GeoObjectRenderer<?> pRenderer) {
        GeoRenderEvent.Object.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Object.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireObjectPreRender(GeoObjectRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return GeoRenderEvent.Object.Pre.EVENT.invoker().handle(new GeoRenderEvent.Object.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireObjectPostRender(GeoObjectRenderer<?> pRenderer, PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        GeoRenderEvent.Object.Post.EVENT.invoker().handle(new GeoRenderEvent.Object.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
}
