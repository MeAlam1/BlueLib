/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;

public class BlockAndItemBlueLayer<T extends BlueAnimatable> extends BlueRenderLayer<T> {

	@NotNull
	protected final BiFunction<BoneCache, T, ItemStack> stackForBone;
	@NotNull
	protected final BiFunction<BoneCache, T, BlockState> blockForBone;

	public BlockAndItemBlueLayer(@NotNull BlueRenderer<T> pRenderer) {
		this(pRenderer, (bone, animatable) -> null, (bone, animatable) -> null);
	}

	public BlockAndItemBlueLayer(@NotNull BlueRenderer<T> pRenderer, @NotNull BiFunction<BoneCache, T, ItemStack> pStackForBone, @NotNull BiFunction<BoneCache, T, BlockState> pBlockForBone) {
		super(pRenderer);

		this.stackForBone = pStackForBone;
		this.blockForBone = pBlockForBone;
	}

	@Nullable
	protected ItemStack getStackForBone(@NotNull BoneCache pBone, @NotNull T pAnimatable) {
		return this.stackForBone.apply(pBone, pAnimatable);
	}

	@Nullable
	protected BlockState getBlockForBone(@NotNull BoneCache pBone, @NotNull T pAnimatable) {
		return this.blockForBone.apply(pBone, pAnimatable);
	}

	@NotNull
	protected ItemDisplayContext getTransformTypeForStack(@NotNull BoneCache pBone, @NotNull ItemStack pStack, @NotNull IRenderContext<T> pContext) {
		return ItemDisplayContext.NONE;
	}

	@Override
	public void renderForBone(@NotNull BoneCache pBone, @NotNull IRenderContext<T> pContext) {
		ItemStack stack = getStackForBone(pBone, pContext.animatable());
		BlockState blockState = getBlockForBone(pBone, pContext.animatable());

		if (stack == null && blockState == null)
			return;

		pContext.poseStack().pushPose();
		RenderUtils.translateAndRotateMatrixForBone(pContext.poseStack(), pBone);

		if (stack != null)
			renderStackForBone(pBone, stack, pContext);

		if (blockState != null)
			renderBlockForBone(pBone, blockState, pContext);

		pContext.poseStack().popPose();
	}

	protected void renderStackForBone(@NotNull BoneCache pBone, @NotNull ItemStack pStack, @NotNull IRenderContext<T> pContext) {
		if (pContext.animatable() instanceof LivingEntity livingEntity) {
			Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, pStack,
					getTransformTypeForStack(pBone, pStack, pContext), false, pContext.poseStack(), pContext.bufferSource(), livingEntity.level(),
					pContext.packedLight(), pContext.packedOverlay(), livingEntity.getId());
		} else {
			Minecraft.getInstance().getItemRenderer().renderStatic(pStack, getTransformTypeForStack(pBone, pStack, pContext),
					pContext.packedLight(), pContext.packedOverlay(), pContext.poseStack(), pContext.bufferSource(), Minecraft.getInstance().level, (int) this.renderer.getInstanceId(pContext));
		}
	}

	protected void renderBlockForBone(@NotNull BoneCache pBone, @NotNull BlockState pState, @NotNull IRenderContext<T> pContext) {
		pContext.poseStack().pushPose();
		pContext.poseStack().translate(-0.25f, -0.25f, -0.25f);
		pContext.poseStack().scale(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(pState, pContext.poseStack(), pContext.bufferSource(), pContext.packedLight(), OverlayTexture.NO_OVERLAY);
		pContext.poseStack().popPose();
	}
}
