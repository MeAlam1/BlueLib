package software.bluelib.loader.renderer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.object.*;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.loading.math.MolangQueries;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;
import software.bluelib.loader.util.Color;
import software.bluelib.loader.util.RenderUtil;

import java.util.List;

//TODO Split sources support

public interface GeoRenderer<T extends GeoAnimatable> {
	
	GeoModel<T> getGeoModel();

	
	T getAnimatable();

	
	default ResourceLocation getTextureLocation(T animatable) {
		return getGeoModel().getTextureResource(animatable, this);
	}

	
	default List<GeoRenderLayer<T>> getRenderLayers() {
		return List.of();
	}

	
	@Nullable
	default RenderType getRenderType(T animatable, ResourceLocation texture,
									 @Nullable MultiBufferSource bufferSource,
									 float partialTick) {
		return getGeoModel().getRenderType(animatable, texture);
	}

	
	default Color getRenderColor(T animatable, float partialTick, int packedLight) {
		return Color.WHITE;
	}

	
	default int getPackedOverlay(T animatable, float u, float partialTick) {
		return OverlayTexture.NO_OVERLAY;
	}

	
	default long getInstanceId(T animatable) {
		return animatable.hashCode();
	}

	
	default float getMotionAnimThreshold(T animatable) {
		return 0.015f;
	}

	
	default void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer,
							   float yaw, float partialTick, int packedLight) {
		poseStack.pushPose();

		int renderColor = getRenderColor(animatable, partialTick, packedLight).argbInt();
		int packedOverlay = getPackedOverlay(animatable, 0, partialTick);
		BakedGeoModel model = getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable, this));

		if (renderType == null)
			renderType = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);

		if (buffer == null && renderType != null)
			buffer = bufferSource.getBuffer(renderType);

		preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, renderColor);

		if (firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
			preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, packedLight, packedLight, packedOverlay);
			actuallyRender(poseStack, animatable, model, renderType,
					bufferSource, buffer, false, partialTick, packedLight, packedOverlay, renderColor);
			applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
			postRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, renderColor);
			firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
		}

		poseStack.popPose();

		renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, renderColor);
		doPostRenderCleanup();
		MolangQueries.clearActor();
	}

	
	default void reRender(BakedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, T animatable,
						  RenderType renderType, VertexConsumer buffer, float partialTick,
						  int packedLight, int packedOverlay, int colour) {
		poseStack.pushPose();
		preRender(poseStack, animatable, model, bufferSource, buffer, true, partialTick, packedLight, packedOverlay, colour);
		actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, true, partialTick, packedLight, packedOverlay, colour);
		postRender(poseStack, animatable, model, bufferSource, buffer, true, partialTick, packedLight, packedOverlay, colour);
		poseStack.popPose();
	}

	
	default void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType,
								MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
								int packedLight, int packedOverlay, int colour) {
		if (buffer == null) {
			if (renderType == null)
				return;

			buffer = bufferSource.getBuffer(renderType);
		}

		updateAnimatedTextureFrame(animatable);

		for (GeoBone group : model.topLevelBones()) {
			renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
					packedOverlay, colour);
		}
	}

	
	default void preApplyRenderLayers(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource,
								   @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		for (GeoRenderLayer<T> renderLayer : getRenderLayers()) {
			renderLayer.preRender(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
		}
	}

	
	default void applyRenderLayersForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource,
										  VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		for (GeoRenderLayer<T> renderLayer : getRenderLayers()) {
			renderLayer.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
		}
	}

	// TODO append renderColor to layers
	
	default void applyRenderLayers(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource,
								   @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		for (GeoRenderLayer<T> renderLayer : getRenderLayers()) {
			renderLayer.render(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
		}
	}

	
	default void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
						   int packedOverlay, int colour) {}

	
	default void postRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {}

	
	default void renderFinal(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight,
							 int packedOverlay, int colour) {}

	
	default void doPostRenderCleanup() {}

	
	default void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource,
								   VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
								   int packedOverlay, int colour) {
		poseStack.pushPose();
		RenderUtil.prepMatrixForBone(poseStack, bone);

		buffer = checkAndRefreshBuffer(isReRender, buffer, bufferSource, renderType);

		renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);

		if (!isReRender)
			applyRenderLayersForBone(poseStack, getAnimatable(), bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

		renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
		poseStack.popPose();
	}

	
	default void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight,
								   int packedOverlay, int colour) {
		if (bone.isHidden())
			return;

		for (GeoCube cube : bone.getCubes()) {
			poseStack.pushPose();
			renderCube(poseStack, cube, buffer, packedLight, packedOverlay, colour);
			poseStack.popPose();
		}
	}

	
	default void renderChildBones(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
								  boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		if (bone.isHidingChildren())
			return;

		for (GeoBone childBone : bone.getChildBones()) {
			renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
		}
	}

	
	default void renderCube(PoseStack poseStack, GeoCube cube, VertexConsumer buffer, int packedLight,
							int packedOverlay, int colour) {
		RenderUtil.translateToPivotPoint(poseStack, cube);
		RenderUtil.rotateMatrixAroundCube(poseStack, cube);
		RenderUtil.translateAwayFromPivotPoint(poseStack, cube);

		Matrix3f normalisedPoseState = poseStack.last().normal();
		Matrix4f poseState = new Matrix4f(poseStack.last().pose());

		for (GeoQuad quad : cube.quads()) {
			if (quad == null)
				continue;

			Vector3f normal = normalisedPoseState.transform(new Vector3f(quad.normal()));
			
			RenderUtil.fixInvertedFlatCube(cube, normal);
			createVerticesOfQuad(quad, poseState, normal, buffer, packedLight, packedOverlay, colour);
		}
	}

	
	default void createVerticesOfQuad(GeoQuad quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer,
									  int packedLight, int packedOverlay, int colour) {
		for (GeoVertex vertex : quad.vertices()) {
			Vector3f position = vertex.position();			
			Vector4f vector4f = poseState.transform(new Vector4f(position.x(), position.y(), position.z(), 1.0f));

			buffer.addVertex(vector4f.x(), vector4f.y(), vector4f.z(), colour, vertex.texU(),
					vertex.texV(), packedOverlay, packedLight, normal.x(), normal.y(), normal.z());
		}
	}

	
	void fireCompileRenderLayersEvent();

	
	boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight);

	
	void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight);
	
    
	default void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, T animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
		if (!isReRender && (widthScale != 1 || heightScale != 1))
			poseStack.scale(widthScale, heightScale, widthScale);
	}

	
	void updateAnimatedTextureFrame(T animatable);

	
	@Deprecated(forRemoval = true)
	@ApiStatus.Internal
	default VertexConsumer checkAndRefreshBuffer(boolean isReRender, VertexConsumer buffer, MultiBufferSource bufferSource, RenderType renderType) {
		if (isReRender)
			return buffer;

		return switch (buffer) {
			case BufferBuilder builder when !builder.building -> bufferSource.getBuffer(renderType);
			case OutlineBufferSource.EntityOutlineGenerator outlines when bufferNeedsRefresh(outlines.delegate()) ->
					new OutlineBufferSource.EntityOutlineGenerator(bufferSource.getBuffer(renderType), outlines.color());
			case VertexMultiConsumer.Double pair when bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second) ->
				new VertexMultiConsumer.Double(bufferNeedsRefresh(pair.first) ? bufferSource.getBuffer(renderType) : pair.first, bufferNeedsRefresh(pair.second) ? bufferSource.getBuffer(renderType) : pair.second);
			default -> buffer;
		};
	}

	@Deprecated(forRemoval = true)
	@ApiStatus.Internal
	private boolean bufferNeedsRefresh(VertexConsumer buffer) {
		return switch (buffer) {
			case BufferBuilder builder -> !builder.building;
			case OutlineBufferSource.EntityOutlineGenerator outlines -> bufferNeedsRefresh(outlines.delegate());
			case VertexMultiConsumer.Double pair -> bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second);
			default -> false;
		};
	}
}
