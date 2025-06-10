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
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.loader.event.BlueRenderEvent;
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
    public void fireCompileBlockRenderLayers(BlueBlockRenderer<?> pRenderer) {
        BlueRenderEvent.Block.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Block.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireBlockPreRender(BlueBlockRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueRenderEvent.Block.Pre.EVENT.invoker().handle(new BlueRenderEvent.Block.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireBlockPostRender(BlueBlockRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueRenderEvent.Block.Post.EVENT.invoker().handle(new BlueRenderEvent.Block.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileArmorRenderLayers(BlueArmorRenderer<?> pRenderer) {
        BlueRenderEvent.Armor.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Armor.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireArmorPreRender(BlueArmorRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueRenderEvent.Armor.Pre.EVENT.invoker().handle(new BlueRenderEvent.Armor.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireArmorPostRender(BlueArmorRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueRenderEvent.Armor.Post.EVENT.invoker().handle(new BlueRenderEvent.Armor.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileEntityRenderLayers(BlueEntityRenderer<?> pRenderer) {
        BlueRenderEvent.Entity.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Entity.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireEntityPreRender(BlueEntityRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueRenderEvent.Entity.Pre.EVENT.invoker().handle(new BlueRenderEvent.Entity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireEntityPostRender(BlueEntityRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueRenderEvent.Entity.Post.EVENT.invoker().handle(new BlueRenderEvent.Entity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileReplacedEntityRenderLayers(BlueReplacedEntityRenderer<?, ?> pRenderer) {
        BlueRenderEvent.ReplacedEntity.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.ReplacedEntity.CompileRenderLayers(pRenderer));
    }

    @Override
    public boolean fireReplacedEntityPreRender(BlueReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueRenderEvent.ReplacedEntity.Pre.EVENT.invoker().handle(new BlueRenderEvent.ReplacedEntity.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireReplacedEntityPostRender(BlueReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueRenderEvent.ReplacedEntity.Post.EVENT.invoker().handle(new BlueRenderEvent.ReplacedEntity.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileItemRenderLayers(BlueItemRenderer<?> pRenderer) {
        BlueRenderEvent.Item.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Item.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireItemPreRender(BlueItemRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueRenderEvent.Item.Pre.EVENT.invoker().handle(new BlueRenderEvent.Item.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireItemPostRender(BlueItemRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueRenderEvent.Item.Post.EVENT.invoker().handle(new BlueRenderEvent.Item.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireCompileObjectRenderLayers(BlueObjectRenderer<?> pRenderer) {
        BlueRenderEvent.Object.CompileRenderLayers.EVENT.invoker().handle(new BlueRenderEvent.Object.CompileRenderLayers(pRenderer));
    }
    
    @Override
    public boolean fireObjectPreRender(BlueObjectRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueRenderEvent.Object.Pre.EVENT.invoker().handle(new BlueRenderEvent.Object.Pre(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
    
    @Override
    public void fireObjectPostRender(BlueObjectRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueRenderEvent.Object.Post.EVENT.invoker().handle(new BlueRenderEvent.Object.Post(pRenderer, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight));
    }
}
