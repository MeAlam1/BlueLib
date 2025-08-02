/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderer;

public class BlockAndItemBlueLayer<T extends BlueAnimatable> extends BlueRenderLayer<T> {

	protected final BiFunction<BoneCache, T, ItemStack> stackForBone;
	protected final BiFunction<BoneCache, T, BlockState> blockForBone;

	public BlockAndItemBlueLayer(BlueRenderer<T> pRenderer) {
		this(pRenderer, (bone, animatable) -> null, (bone, animatable) -> null);
	}

	public BlockAndItemBlueLayer(BlueRenderer<T> pRenderer, BiFunction<BoneCache, T, ItemStack> pStackForBone, BiFunction<BoneCache, T, BlockState> pBlockForBone) {
		super(pRenderer);

		this.stackForBone = pStackForBone;
		this.blockForBone = pBlockForBone;
	}

	@Nullable
	protected ItemStack getStackForBone(BoneCache pBone, T pAnimatable) {
		return this.stackForBone.apply(pBone, pAnimatable);
	}

	@Nullable
	protected BlockState getBlockForBone(BoneCache pBone, T pAnimatable) {
		return this.blockForBone.apply(pBone, pAnimatable);
	}

	protected ItemDisplayContext getTransformTypeForStack(BoneCache pBone, ItemStack pStack, T pAnimatable) {
		return ItemDisplayContext.NONE;
	}

	@Override
	public void renderForBone(PoseStack pPoseStack, T pAnimatable, BoneCache pBone, RenderType pRenderType, MultiBufferSource pBufferSource,
			VertexConsumer pBuffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
		ItemStack stack = getStackForBone(pBone, pAnimatable);
		BlockState blockState = getBlockForBone(pBone, pAnimatable);

		if (stack == null && blockState == null)
			return;

		pPoseStack.pushPose();
		RenderUtils.translateAndRotateMatrixForBone(pPoseStack, pBone);

		if (stack != null)
			renderStackForBone(pPoseStack, pBone, stack, pAnimatable, pBufferSource, pPartialTick, pPackedLight, pPackedOverlay);

		if (blockState != null)
			renderBlockForBone(pPoseStack, pBone, blockState, pAnimatable, pBufferSource, pPartialTick, pPackedLight, pPackedOverlay);

		pPoseStack.popPose();
	}

	protected void renderStackForBone(PoseStack pPoseStack, BoneCache pBone, ItemStack pStack, T pAnimatable, MultiBufferSource pBufferSource,
			float pPartialTick, int pPackedLight, int pPackedOverlay) {
		if (pAnimatable instanceof LivingEntity livingEntity) {
			Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, pStack,
					getTransformTypeForStack(pBone, pStack, pAnimatable), false, pPoseStack, pBufferSource, livingEntity.level(),
					pPackedLight, pPackedOverlay, livingEntity.getId());
		} else {
			Minecraft.getInstance().getItemRenderer().renderStatic(pStack, getTransformTypeForStack(pBone, pStack, pAnimatable),
					pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, Minecraft.getInstance().level, (int) this.renderer.getInstanceId(pAnimatable));
		}
	}

	protected void renderBlockForBone(PoseStack pPoseStack, BoneCache pBone, BlockState pState, T pAnimatable, MultiBufferSource pBufferSource,
			float pPartialTick, int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		pPoseStack.translate(-0.25f, -0.25f, -0.25f);
		pPoseStack.scale(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(pState, pPoseStack, pBufferSource, pPackedLight, OverlayTexture.NO_OVERLAY);
		pPoseStack.popPose();
	}
}
