/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.layer;

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
import software.bluelib.loader.renderer.base.BlueRenderer;

public class BlockAndItemBlueLayer<T extends BlueAnimatable> extends BlueRenderLayer<T> {

	protected final BiFunction<BoneCache, T, ItemStack> stackForBone;
	protected final BiFunction<BoneCache, T, BlockState> blockForBone;

	public BlockAndItemBlueLayer(BlueRenderer<T> renderer) {
		this(renderer, (bone, animatable) -> null, (bone, animatable) -> null);
	}

	public BlockAndItemBlueLayer(BlueRenderer<T> renderer, BiFunction<BoneCache, T, ItemStack> stackForBone, BiFunction<BoneCache, T, BlockState> blockForBone) {
		super(renderer);

		this.stackForBone = stackForBone;
		this.blockForBone = blockForBone;
	}

	@Nullable
	protected ItemStack getStackForBone(BoneCache bone, T animatable) {
		return this.stackForBone.apply(bone, animatable);
	}

	@Nullable
	protected BlockState getBlockForBone(BoneCache bone, T animatable) {
		return this.blockForBone.apply(bone, animatable);
	}

	protected ItemDisplayContext getTransformTypeForStack(BoneCache bone, ItemStack stack, T animatable) {
		return ItemDisplayContext.NONE;
	}

	@Override
	public void renderForBone(PoseStack pPoseStack, T animatable, BoneCache bone, RenderType pRenderType, MultiBufferSource pBufferSource,
			VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
		ItemStack stack = getStackForBone(bone, animatable);
		BlockState blockState = getBlockForBone(bone, animatable);

		if (stack == null && blockState == null)
			return;

		pPoseStack.pushPose();
		RenderUtils.translateAndRotateMatrixForBone(pPoseStack, bone);

		if (stack != null)
			renderStackForBone(pPoseStack, bone, stack, animatable, pBufferSource, pPartialTick, pPackedLight, pPackedOverlay);

		if (blockState != null)
			renderBlockForBone(pPoseStack, bone, blockState, animatable, pBufferSource, pPartialTick, pPackedLight, pPackedOverlay);

		pPoseStack.popPose();
	}

	protected void renderStackForBone(PoseStack pPoseStack, BoneCache bone, ItemStack stack, T animatable, MultiBufferSource pBufferSource,
			float pPartialTick, int pPackedLight, int pPackedOverlay) {
		if (animatable instanceof LivingEntity livingEntity) {
			Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, stack,
					getTransformTypeForStack(bone, stack, animatable), false, pPoseStack, pBufferSource, livingEntity.level(),
					pPackedLight, pPackedOverlay, livingEntity.getId());
		} else {
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, getTransformTypeForStack(bone, stack, animatable),
					pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, Minecraft.getInstance().level, (int) this.renderer.getInstanceId(animatable));
		}
	}

	protected void renderBlockForBone(PoseStack pPoseStack, BoneCache bone, BlockState state, T animatable, MultiBufferSource pBufferSource,
			float pPartialTick, int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		pPoseStack.translate(-0.25f, -0.25f, -0.25f);
		pPoseStack.scale(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, pPoseStack, pBufferSource, pPackedLight, OverlayTexture.NO_OVERLAY);
		pPoseStack.popPose();
	}
}
