/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.specialty;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.json.object.QuadData;
import software.bluelib.loader.json.object.VertexData;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.BlueEntityRenderer;

public abstract class DynamicBlueEntityRenderer<T extends Entity & BlueAnimatable> extends BlueEntityRenderer<T> {

	protected static Map<ResourceLocation, IntIntPair> TEXTURE_DIMENSIONS_CACHE = new Object2ObjectOpenHashMap<>();

	protected ResourceLocation textureOverride = null;

	public DynamicBlueEntityRenderer(EntityRendererProvider.Context renderManager, BlueModel<T> model) {
		super(renderManager, model);
	}

	@Nullable
	protected ResourceLocation getTextureOverrideForBone(BoneCache bone, T animatable, float pPartialTick) {
		return null;
	}

	@Nullable
	protected RenderType getRenderTypeOverrideForBone(BoneCache bone, T animatable, ResourceLocation texturePath, MultiBufferSource pBufferSource, float pPartialTick) {
		return null;
	}

	protected boolean boneRenderOverride(PoseStack pPoseStack, BoneCache bone, MultiBufferSource pBufferSource, VertexConsumer buffer,
			float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
		return false;
	}

	@Override
	public void renderRecursively(PoseStack pPoseStack, T animatable, BoneCache bone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
		pPoseStack.pushPose();
		RenderUtils.translateMatrixToBone(pPoseStack, bone);
		RenderUtils.translateToPivotPoint(pPoseStack, bone);
		RenderUtils.rotateMatrixAroundBone(pPoseStack, bone);
		RenderUtils.scaleMatrixForBone(pPoseStack, bone);

		if (bone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());
			Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

			bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			localMatrix.translate(new Vector3f(getRenderOffset(this.animatable, 1).toVector3f()));
			bone.setLocalSpaceMatrix(localMatrix);

			Matrix4f worldState = new Matrix4f(localMatrix);

			worldState.translate(new Vector3f(this.animatable.position().toVector3f()));
			bone.setWorldSpaceMatrix(worldState);
		}

		RenderUtils.translateAwayFromPivotPoint(pPoseStack, bone);

		this.textureOverride = getTextureOverrideForBone(bone, this.animatable, pPartialTick);
		ResourceLocation texture = this.textureOverride == null ? getTextureLocation(this.animatable) : this.textureOverride;
		RenderType renderTypeOverride = getRenderTypeOverrideForBone(bone, this.animatable, texture, pBufferSource, pPartialTick);

		if (texture != null && renderTypeOverride == null)
			renderTypeOverride = getRenderType(this.animatable, texture, pBufferSource, pPartialTick);

		if (renderTypeOverride != null)
			buffer = pBufferSource.getBuffer(renderTypeOverride);

		if (!boneRenderOverride(pPoseStack, bone, pBufferSource, buffer, pPartialTick, pPackedLight, pPackedOverlay, colour))
			super.renderCubesOfBone(pPoseStack, bone, buffer, pPackedLight, pPackedOverlay, colour);

		if (renderTypeOverride != null)
			buffer = pBufferSource.getBuffer(pRenderType);

		if (!pIsReRender)
			applyRenderLayersForBone(pPoseStack, animatable, bone, pRenderType, pBufferSource, buffer, pPartialTick, pPackedLight, pPackedOverlay);

		buffer = checkAndRefreshBuffer(pIsReRender, buffer, pBufferSource, pRenderType);

		super.renderChildBones(pPoseStack, animatable, bone, pRenderType, pBufferSource, buffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, colour);

		pPoseStack.popPose();
	}

	@Override
	public void postRender(PoseStack pPoseStack, T animatable, ModelCache model, MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
		this.textureOverride = null;

		super.postRender(pPoseStack, animatable, model, pBufferSource, buffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, colour);
	}

	@Override
	public void createVerticesOfQuad(QuadData quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer,
			int pPackedLight, int pPackedOverlay, int colour) {
		if (this.textureOverride == null) {
			super.createVerticesOfQuad(quad, poseState, normal, buffer, pPackedLight, pPackedOverlay,
					colour);

			return;
		}

		IntIntPair boneTextureSize = computeTextureSize(this.textureOverride);
		IntIntPair entityTextureSize = computeTextureSize(getTextureLocation(this.animatable));

		if (boneTextureSize == null || entityTextureSize == null) {
			super.createVerticesOfQuad(quad, poseState, normal, buffer, pPackedLight, pPackedOverlay,
					colour);

			return;
		}

		for (VertexData vertex : quad.vertices()) {
			Vector4f vector4f = poseState.transform(new Vector4f(vertex.position().x(), vertex.position().y(), vertex.position().z(), 1.0f));
			float texU = (vertex.texU() * entityTextureSize.firstInt()) / boneTextureSize.firstInt();
			float texV = (vertex.texV() * entityTextureSize.secondInt()) / boneTextureSize.secondInt();

			buffer.addVertex(vector4f.x(), vector4f.y(), vector4f.z(), colour, texU, texV,
					pPackedOverlay, pPackedLight, normal.x(), normal.y(), normal.z());
		}
	}

	protected IntIntPair computeTextureSize(ResourceLocation texture) {
		return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(texture, RenderUtils::getTextureDimensions);
	}
}
