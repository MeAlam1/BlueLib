/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.block;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bluelib.api.utils.loader.BufferUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.json.object.QuadData;
import software.bluelib.loader.json.object.VertexData;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public abstract class DynamicBlueBlockRenderer<T extends BlockEntity & BlueAnimatable> extends BlueBlockRenderer<T> {

	@NotNull
	protected static Map<ResourceLocation, IntIntPair> TEXTURE_DIMENSIONS_CACHE = new Object2ObjectOpenHashMap<>();
	@Nullable
	protected ResourceLocation textureOverride = null;

	public DynamicBlueBlockRenderer(@NotNull BlueModel<T> pModel) {
		super(pModel);
	}

	@Nullable
	protected ResourceLocation getTextureOverrideForBone(@NotNull BoneCache pBone, @NotNull IRenderContext<T> pContext) {
		return null;
	}

	@Nullable
	protected RenderType getRenderTypeOverrideForBone(@NotNull BoneCache pBone, @NotNull ResourceLocation pTexturePath, @NotNull IRenderContext<T> pContext) {
		return null;
	}

	protected boolean boneRenderOverride(@NotNull BoneCache pBone, @NotNull IRenderContext<T> pContext) {
		return false;
	}

	@Override
	public void renderRecursively(@NotNull BoneCache pBone, @NotNull FullRenderContext<T> pContext) {
		pContext.poseStack().pushPose();
		RenderUtils.translateMatrixToBone(pContext.poseStack(), pBone);
		RenderUtils.translateToPivotPoint(pContext.poseStack(), pBone);
		RenderUtils.rotateMatrixAroundBone(pContext.poseStack(), pBone);
		RenderUtils.scaleMatrixForBone(pContext.poseStack(), pBone);

		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pContext.poseStack().last().pose());
			Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
			Matrix4f worldState = new Matrix4f(localMatrix);
			BlockPos pos = getAnimatable().getBlockPos();

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(localMatrix);
			pBone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
		}

		RenderUtils.translateAwayFromPivotPoint(pContext.poseStack(), pBone);

		this.textureOverride = getTextureOverrideForBone(pBone, pContext);
		ResourceLocation texture = this.textureOverride == null ? getTextureLocation(getAnimatable()) : this.textureOverride;
		RenderType renderTypeOverride = getRenderTypeOverrideForBone(pBone, texture, pContext);

		if (renderTypeOverride == null)
			renderTypeOverride = getRenderType(texture, pContext);

		if (renderTypeOverride != null)
			pContext.setBuffer(pContext.bufferSource().getBuffer(renderTypeOverride));

		if (!boneRenderOverride(pBone, pContext))
			super.renderCubesOfBone(pBone, pContext);

		if (renderTypeOverride != null)
			pContext.setBuffer(pContext.bufferSource().getBuffer(pContext.renderType()));

		if (!pContext.isReRender())
			applyRenderLayersForBone(pBone, pContext);

		pContext.setBuffer(BufferUtils.checkAndRefreshBuffer(pContext.isReRender(), pContext.buffer(), pContext.bufferSource(), pContext.renderType()));

		super.renderChildBones(pBone, pContext);

		pContext.poseStack().popPose();
	}

	@Override
	public void postRender(@NotNull IRenderContext<T> pContext) {
		this.textureOverride = null;

		super.postRender(pContext);
	}

	@Override
	public void createVerticesOfQuad(@NotNull QuadData pQuad, @NotNull Matrix4f pPoseState, @NotNull Vector3f pNormal, @NotNull FullRenderContext<T> pContext) {
		if (this.textureOverride == null) {
			super.createVerticesOfQuad(pQuad, pPoseState, pNormal, pContext);

			return;
		}

		IntIntPair boneTextureSize = computeTextureSize(this.textureOverride);
		IntIntPair blockTextureSize = computeTextureSize(getTextureLocation(getAnimatable()));

		if (boneTextureSize == null || blockTextureSize == null) {
			super.createVerticesOfQuad(pQuad, pPoseState, pNormal, pContext);

			return;
		}

		for (VertexData vertex : pQuad.vertices()) {
			Vector4f vector4f = pPoseState.transform(new Vector4f(vertex.position().x(), vertex.position().y(), vertex.position().z(), 1.0f));
			float texU = (vertex.texU() * blockTextureSize.firstInt()) / boneTextureSize.firstInt();
			float texV = (vertex.texV() * blockTextureSize.secondInt()) / boneTextureSize.secondInt();

			pContext.buffer().addVertex(vector4f.x(), vector4f.y(), vector4f.z(), pContext.color(), texU, texV,
					pContext.packedOverlay(), pContext.packedLight(), pNormal.x(), pNormal.y(), pNormal.z());
		}
	}

	@Nullable
	protected IntIntPair computeTextureSize(@NotNull ResourceLocation pTexture) {
		return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(pTexture, RenderUtils::getTextureDimensions);
	}
}
