/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bluelib.api.utils.Color;
import software.bluelib.api.utils.loader.BufferUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.CubeCache;
import software.bluelib.loader.geckolib.math.MoLangQueries;
import software.bluelib.loader.json.object.QuadData;
import software.bluelib.loader.json.object.VertexData;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.context.BaseRenderContext;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;

public interface BlueRenderer<T extends BlueAnimatable> {

	BlueModel<T> getBlueModel();

	T getAnimatable();

	default ResourceLocation getTextureLocation(T pAnimatable) {
		return getBlueModel().getTextureResource(pAnimatable, this);
	}

	default List<BlueRenderLayer<T>> getRenderLayers() {
		return List.of();
	}

	@Nullable
	default RenderType getRenderType(ResourceLocation pTexture, IRenderContext<T> pContext) {
		return getBlueModel().getRenderType(pContext.animatable(), pTexture);
	}

	default Color getRenderColor(T pAnimatable, float pPartialTick, int pPackedLight) {
		return Color.WHITE;
	}

	default int getPackedOverlay(T pAnimatable, float pU, float pPartialTick) {
		return OverlayTexture.NO_OVERLAY;
	}

	default long getInstanceId(IRenderContext<T> pContext) {
		return pContext.animatable().hashCode();
	}

	default float getMotionAnimThreshold(IRenderContext<T> pContext) {
		return 0.015f;
	}

	private static <M extends BlueAnimatable> void handleBaseRenderContextInternal(
			BaseRenderContext<M> pContext,
			BlueRenderer<M> pRenderer,
			BiConsumer<BlueRenderer<M>, FullRenderContext<M>> pRenderMethod) {
		RenderType type = pRenderer.getRenderType(
				pRenderer.getTextureLocation(pContext.animatable()),
				pContext);
		VertexConsumer buffer = type != null ? pContext.bufferSource().getBuffer(type) : null;
		if (type == null) {
			pContext.poseStack().popPose();
			return;
		}
		FullRenderContext<M> full = new FullRenderContext<>(
				pContext.poseStack(),
				pContext.animatable(),
				pContext.model(),
				type,
				pContext.bufferSource(),
				buffer,
				pContext.isReRender(),
				pContext.partialTick(),
				pContext.packedLight(),
				pContext.packedOverlay(),
				pContext.color());
		pRenderMethod.accept(pRenderer, full);
		pContext.poseStack().popPose();
	}

	default void defaultRender(IRenderContext<T> pContext) {
		pContext.poseStack().pushPose();

		if (pContext instanceof FullRenderContext<T> full) {
			preRender(full);

			if (firePreRenderEvent(full)) {
				preApplyRenderLayers(full);
				actuallyRender(full);
				applyRenderLayers(full);
				postRender(full);
				firePostRenderEvent(full);
			}

			full.poseStack().popPose();

			renderFinal(full);
			doPostRenderCleanup(full);
			MoLangQueries.clearActor();
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseDefaultRenderContext(base, this);
		}
	}

	@ApiStatus.NonExtendable
	default <M extends BlueAnimatable> void handleBaseDefaultRenderContext(BaseRenderContext<M> pContext, BlueRenderer<M> pRenderer) {
		handleBaseRenderContextInternal(pContext, pRenderer, BlueRenderer::defaultRender);
	}

	default void reRender(IRenderContext<T> pContext) {
		pContext.poseStack().pushPose();

		if (pContext instanceof FullRenderContext<T> full) {
			preRender(full);
			actuallyRender(full);
			postRender(full);
			full.poseStack().popPose();
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseReRenderContext(base, this);
		}
	}

	@ApiStatus.NonExtendable
	default <M extends BlueAnimatable> void handleBaseReRenderContext(BaseRenderContext<M> pContext, BlueRenderer<M> pRenderer) {
		handleBaseRenderContextInternal(pContext, pRenderer, BlueRenderer::reRender);
	}

	default void actuallyRender(IRenderContext<T> pContext) {
		if (pContext instanceof FullRenderContext<T> full) {
			if (full.buffer() == null) {
				if (full.renderType() == null)
					return;
				VertexConsumer buffer = full.bufferSource().getBuffer(full.renderType());
				full = new FullRenderContext<>(
						full.poseStack(),
						full.animatable(),
						full.model(),
						full.renderType(),
						full.bufferSource(),
						buffer,
						full.isReRender(),
						full.partialTick(),
						full.packedLight(),
						full.packedOverlay(),
						full.color());
			}

			updateAnimatedTextureFrame(full.animatable());

			for (BoneCache group : full.model().topLevelBones()) {
				renderRecursively(
						group,
						full);
			}
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseActuallyRenderContext(base, this);
		}
	}

	@ApiStatus.NonExtendable
	default <M extends BlueAnimatable> void handleBaseActuallyRenderContext(BaseRenderContext<M> pContext, BlueRenderer<M> pRenderer) {
		handleBaseRenderContextInternal(pContext, pRenderer, BlueRenderer::actuallyRender);
	}

	default void preApplyRenderLayers(IRenderContext<T> pContext) {
		for (BlueRenderLayer<T> renderLayer : getRenderLayers()) {
			renderLayer.preRender(pContext);
		}
	}

	default void applyRenderLayersForBone(BoneCache pBone, IRenderContext<T> pContext) {
		for (BlueRenderLayer<T> renderLayer : getRenderLayers()) {
			renderLayer.renderForBone(pBone, pContext);
		}
	}

	default void applyRenderLayers(IRenderContext<T> pContext) {
		for (BlueRenderLayer<T> renderLayer : getRenderLayers()) {
			renderLayer.render(pContext);
		}
	}

	default void preRender(IRenderContext<T> pContext) {}

	default void postRender(IRenderContext<T> pContext) {}

	default void renderFinal(IRenderContext<T> pContext) {}

	default void doPostRenderCleanup(IRenderContext<T> pContext) {}

	default void renderRecursively(BoneCache pBone, FullRenderContext<T> pContext) {
		pContext.poseStack().pushPose();
		RenderUtils.prepMatrixForBone(pContext.poseStack(), pBone);

		pContext.setBuffer(BufferUtils.checkAndRefreshBuffer(pContext.isReRender(), pContext.buffer(), pContext.bufferSource(), pContext.renderType()));

		renderCubesOfBone(pBone, pContext);

		if (!pContext.isReRender())
			applyRenderLayersForBone(pBone, pContext);

		renderChildBones(pBone, pContext);
		pContext.poseStack().popPose();
	}

	default void renderCubesOfBone(BoneCache pBone, FullRenderContext<T> pContext) {
		if (pBone.isHidden())
			return;

		for (CubeCache cube : pBone.getCubes()) {
			pContext.poseStack().pushPose();
			renderCube(cube, pContext);
			pContext.poseStack().popPose();
		}
	}

	default void renderChildBones(BoneCache pBone, FullRenderContext<T> pContext) {
		if (pBone.isHidingChildren())
			return;

		for (BoneCache childBone : pBone.getChildBones()) {
			renderRecursively(childBone, pContext);
		}
	}

	default void renderCube(CubeCache pCube, FullRenderContext<T> pContext) {
		RenderUtils.translateToPivotPoint(pContext.poseStack(), pCube);
		RenderUtils.rotateMatrixAroundCube(pContext.poseStack(), pCube);
		RenderUtils.translateAwayFromPivotPoint(pContext.poseStack(), pCube);

		Matrix3f normalisedPoseState = pContext.poseStack().last().normal();
		Matrix4f poseState = new Matrix4f(pContext.poseStack().last().pose());

		for (QuadData quad : pCube.quads()) {
			if (quad == null)
				continue;

			Vector3f normal = normalisedPoseState.transform(new Vector3f(quad.normal()));

			RenderUtils.fixInvertedFlatCube(pCube, normal);
			createVerticesOfQuad(quad, poseState, normal, pContext);
		}
	}

	default void createVerticesOfQuad(QuadData pQuad, Matrix4f pPoseState, Vector3f pNormal, FullRenderContext<T> pContext) {
		for (VertexData vertex : pQuad.vertices()) {
			Vector3f position = vertex.position();
			Vector4f vector4f = pPoseState.transform(new Vector4f(position.x(), position.y(), position.z(), 1.0f));

			pContext.buffer().addVertex(vector4f.x(), vector4f.y(), vector4f.z(), pContext.color(), vertex.texU(),
					vertex.texV(), pContext.packedOverlay(), pContext.packedLight(), pNormal.x(), pNormal.y(), pNormal.z());
		}
	}

	void fireCompileRenderLayersEvent();

	boolean firePreRenderEvent(IRenderContext<T> pContext);

	void firePostRenderEvent(IRenderContext<T> pContext);

	default void scaleModelForRender(float pWidthScale, float pHeightScale, IRenderContext<T> pContext) {
		if (!pContext.isReRender() && (pWidthScale != 1 || pHeightScale != 1))
			pContext.poseStack().scale(pWidthScale, pHeightScale, pWidthScale);
	}

	void updateAnimatedTextureFrame(T pAnimatable);
}
