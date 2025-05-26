package software.bluelib.loader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.renderer.GeoRenderer;
import software.bluelib.loader.util.RenderUtil;

import java.util.function.BiFunction;


public class BlockAndItemGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
    protected final BiFunction<GeoBone, T, ItemStack> stackForBone;
    protected final BiFunction<GeoBone, T, BlockState> blockForBone;

    public BlockAndItemGeoLayer(GeoRenderer<T> renderer) {
        this(renderer, (bone, animatable) -> null, (bone, animatable) -> null);
    }

    public BlockAndItemGeoLayer(GeoRenderer<T> renderer, BiFunction<GeoBone, T, ItemStack> stackForBone, BiFunction<GeoBone, T, BlockState> blockForBone) {
        super(renderer);

        this.stackForBone = stackForBone;
        this.blockForBone = blockForBone;
    }

    
    @Nullable
    protected ItemStack getStackForBone(GeoBone bone, T animatable) {
        return this.stackForBone.apply(bone, animatable);
    }

    
    @Nullable
    protected BlockState getBlockForBone(GeoBone bone, T animatable) {
        return this.blockForBone.apply(bone, animatable);
    }

    
    protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
        return ItemDisplayContext.NONE;
    }

    
    @Override
    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource,
                              VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ItemStack stack = getStackForBone(bone, animatable);
        BlockState blockState = getBlockForBone(bone, animatable);

        if (stack == null && blockState == null)
            return;

        poseStack.pushPose();
        RenderUtil.translateAndRotateMatrixForBone(poseStack, bone);

        if (stack != null)
            renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);

        if (blockState != null)
            renderBlockForBone(poseStack, bone, blockState, animatable, bufferSource, partialTick, packedLight, packedOverlay);

        poseStack.popPose();
    }

    
    protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource,
                                      float partialTick, int packedLight, int packedOverlay) {
        if (animatable instanceof LivingEntity livingEntity) {
            Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, stack,
                    getTransformTypeForStack(bone, stack, animatable), false, poseStack, bufferSource, livingEntity.level(),
                    packedLight, packedOverlay, livingEntity.getId());
        } else {
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, getTransformTypeForStack(bone, stack, animatable),
                    packedLight, packedOverlay, poseStack, bufferSource, Minecraft.getInstance ().level, (int) this.renderer.getInstanceId(animatable));
        }
    }

    
    protected void renderBlockForBone(PoseStack poseStack, GeoBone bone, BlockState state, T animatable, MultiBufferSource bufferSource,
                                      float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(-0.25f, -0.25f, -0.25f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
