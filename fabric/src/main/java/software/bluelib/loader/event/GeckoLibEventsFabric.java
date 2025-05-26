package software.bluelib.loader.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.renderer.*;
import software.bluelib.loader.service.GeckoLibEvents;


public class GeckoLibEventsFabric implements GeckoLibEvents {
    
    @Override
    public void fireCompileBlockRenderLayers(GeoBlockRenderer<?> renderer) {
        GeoRenderEvent.Block.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Block.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireBlockPreRender(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return GeoRenderEvent.Block.Pre.EVENT.invoker().handle(new GeoRenderEvent.Block.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireBlockPostRender(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        GeoRenderEvent.Block.Post.EVENT.invoker().handle(new GeoRenderEvent.Block.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileArmorRenderLayers(GeoArmorRenderer<?> renderer) {
        GeoRenderEvent.Armor.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Armor.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireArmorPreRender(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return GeoRenderEvent.Armor.Pre.EVENT.invoker().handle(new GeoRenderEvent.Armor.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireArmorPostRender(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        GeoRenderEvent.Armor.Post.EVENT.invoker().handle(new GeoRenderEvent.Armor.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileEntityRenderLayers(GeoEntityRenderer<?> renderer) {
        GeoRenderEvent.Entity.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Entity.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireEntityPreRender(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return GeoRenderEvent.Entity.Pre.EVENT.invoker().handle(new GeoRenderEvent.Entity.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireEntityPostRender(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        GeoRenderEvent.Entity.Post.EVENT.invoker().handle(new GeoRenderEvent.Entity.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileReplacedEntityRenderLayers(GeoReplacedEntityRenderer<?, ?> renderer) {
        GeoRenderEvent.ReplacedEntity.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.ReplacedEntity.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireReplacedEntityPreRender(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return GeoRenderEvent.ReplacedEntity.Pre.EVENT.invoker().handle(new GeoRenderEvent.ReplacedEntity.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireReplacedEntityPostRender(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        GeoRenderEvent.ReplacedEntity.Post.EVENT.invoker().handle(new GeoRenderEvent.ReplacedEntity.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileItemRenderLayers(GeoItemRenderer<?> renderer) {
        GeoRenderEvent.Item.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Item.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireItemPreRender(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return GeoRenderEvent.Item.Pre.EVENT.invoker().handle(new GeoRenderEvent.Item.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireItemPostRender(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        GeoRenderEvent.Item.Post.EVENT.invoker().handle(new GeoRenderEvent.Item.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireCompileObjectRenderLayers(GeoObjectRenderer<?> renderer) {
        GeoRenderEvent.Object.CompileRenderLayers.EVENT.invoker().handle(new GeoRenderEvent.Object.CompileRenderLayers(renderer));
    }

    
    @Override
    public boolean fireObjectPreRender(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return GeoRenderEvent.Object.Pre.EVENT.invoker().handle(new GeoRenderEvent.Object.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    
    @Override
    public void fireObjectPostRender(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
       GeoRenderEvent.Object.Post.EVENT.invoker().handle(new GeoRenderEvent.Object.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }
}
