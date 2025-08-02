/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bluelib.api.utils.loader.BufferUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.json.object.QuadData;
import software.bluelib.loader.json.object.VertexData;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.model.BlueModel;

public abstract class DynamicBlueBlockRenderer<T extends BlockEntity & BlueAnimatable> extends BlueBlockRenderer<T> {

	protected static Map<ResourceLocation, IntIntPair> TEXTURE_DIMENSIONS_CACHE = new Object2ObjectOpenHashMap<>();

	protected ResourceLocation textureOverride = null;

	public DynamicBlueBlockRenderer(BlueModel<T> pModel) {
		super(pModel);
	}

	@Nullable
	protected ResourceLocation getTextureOverrideForBone(BoneCache pBone, T pAnimatable, float pPartialTick) {
		return null;
	}

	@Nullable
	protected RenderType getRenderTypeOverrideForBone(BoneCache pBone, T pAnimatable, ResourceLocation pTexturePath, MultiBufferSource pBufferSource, float pPartialTick) {
		return null;
	}

	protected boolean boneRenderOverride(PoseStack pPoseStack, BoneCache pBone, MultiBufferSource pBufferSource, VertexConsumer pBuffer,
			float pPartialTick, int pPackedLight, int pPackedOverlay, int pColour) {
		return false;
	}

	@Override
	public void renderRecursively(PoseStack pPoseStack, T pAnimatable, BoneCache pBone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int pColour) {
		pPoseStack.pushPose();
		RenderUtils.translateMatrixToBone(pPoseStack, pBone);
		RenderUtils.translateToPivotPoint(pPoseStack, pBone);
		RenderUtils.rotateMatrixAroundBone(pPoseStack, pBone);
		RenderUtils.scaleMatrixForBone(pPoseStack, pBone);

		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());
			Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
			Matrix4f worldState = new Matrix4f(localMatrix);
			BlockPos pos = this.animatable.getBlockPos();

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(localMatrix);
			pBone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
		}

		RenderUtils.translateAwayFromPivotPoint(pPoseStack, pBone);

		this.textureOverride = getTextureOverrideForBone(pBone, this.animatable, pPartialTick);
		ResourceLocation texture = this.textureOverride == null ? getTextureLocation(this.animatable) : this.textureOverride;
		RenderType renderTypeOverride = getRenderTypeOverrideForBone(pBone, this.animatable, texture, pBufferSource, pPartialTick);

		if (texture != null && renderTypeOverride == null)
			renderTypeOverride = getRenderType(this.animatable, texture, pBufferSource, pPartialTick);

		if (renderTypeOverride != null)
			pBuffer = pBufferSource.getBuffer(renderTypeOverride);

		if (!boneRenderOverride(pPoseStack, pBone, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay, pColour))
			super.renderCubesOfBone(pPoseStack, pBone, pBuffer, pPackedLight, pPackedOverlay, pColour);

		if (renderTypeOverride != null)
			pBuffer = pBufferSource.getBuffer(pRenderType);

		if (!pIsReRender)
			applyRenderLayersForBone(pPoseStack, pAnimatable, pBone, pRenderType, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay);

		pBuffer = BufferUtils.checkAndRefreshBuffer(pIsReRender, pBuffer, pBufferSource, pRenderType);

		super.renderChildBones(pPoseStack, pAnimatable, pBone, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, pColour);

		pPoseStack.popPose();
	}

	@Override
	public void postRender(IRenderContext<T> pContext) {
		this.textureOverride = null;

		super.postRender(pContext);
	}

	@Override
	public void createVerticesOfQuad(QuadData pQuad, Matrix4f pPoseState, Vector3f pNormal, VertexConsumer pBuffer,
			int pPackedLight, int pPackedOverlay, int pColour) {
		if (this.textureOverride == null) {
			super.createVerticesOfQuad(pQuad, pPoseState, pNormal, pBuffer, pPackedLight, pPackedOverlay,
					pColour);

			return;
		}

		IntIntPair boneTextureSize = computeTextureSize(this.textureOverride);
		IntIntPair blockTextureSize = computeTextureSize(getTextureLocation(this.animatable));

		if (boneTextureSize == null || blockTextureSize == null) {
			super.createVerticesOfQuad(pQuad, pPoseState, pNormal, pBuffer, pPackedLight, pPackedOverlay,
					pColour);

			return;
		}

		for (VertexData vertex : pQuad.vertices()) {
			Vector4f vector4f = pPoseState.transform(new Vector4f(vertex.position().x(), vertex.position().y(), vertex.position().z(), 1.0f));
			float texU = (vertex.texU() * blockTextureSize.firstInt()) / boneTextureSize.firstInt();
			float texV = (vertex.texV() * blockTextureSize.secondInt()) / boneTextureSize.secondInt();

			pBuffer.addVertex(vector4f.x(), vector4f.y(), vector4f.z(), pColour, texU, texV,
					pPackedOverlay, pPackedLight, pNormal.x(), pNormal.y(), pNormal.z());
		}
	}

	protected IntIntPair computeTextureSize(ResourceLocation pTexture) {
		return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(pTexture, RenderUtils::getTextureDimensions);
	}
}
