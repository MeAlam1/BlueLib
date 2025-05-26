package software.bluelib.loader.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.neoforge.common.NeoForge;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.renderer.*;
import software.bluelib.loader.service.GeckoLibEvents;


public class GeckoLibEventsNeoForge implements GeckoLibEvents {
    
    @Override
    public void fireCompileBlockRenderLayers(GeoBlockRenderer<?> renderer) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Block.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireBlockPreRender(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Block.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight)).isCanceled();
    }

    
    @Override
    public void fireBlockPostRender(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Block.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileArmorRenderLayers(GeoArmorRenderer<?> renderer) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Armor.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireArmorPreRender(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Armor.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight)).isCanceled();
    }

    
    @Override
    public void fireArmorPostRender(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Armor.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileEntityRenderLayers(GeoEntityRenderer<?> renderer) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Entity.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireEntityPreRender(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Entity.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight)).isCanceled();
    }

    
    @Override
    public void fireEntityPostRender(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Entity.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileReplacedEntityRenderLayers(GeoReplacedEntityRenderer<?, ?> renderer) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireReplacedEntityPreRender(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight)).isCanceled();
    }

    
    @Override
    public void fireReplacedEntityPostRender(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileItemRenderLayers(GeoItemRenderer<?> renderer) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Item.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireItemPreRender(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Item.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight)).isCanceled();
    }

    
    @Override
    public void fireItemPostRender(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Item.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileObjectRenderLayers(GeoObjectRenderer<?> renderer) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Object.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireObjectPreRender(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !NeoForge.EVENT_BUS.post(new GeoRenderEvent.Object.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight)).isCanceled();
    }

    
    @Override
    public void fireObjectPostRender(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        NeoForge.EVENT_BUS.post(new GeoRenderEvent.Object.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }
}
