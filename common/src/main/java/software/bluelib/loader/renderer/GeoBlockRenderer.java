package software.bluelib.loader.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;
import software.bluelib.loader.renderer.layer.GeoRenderLayersContainer;
import software.bluelib.loader.util.RenderUtil;

import java.util.List;


public class GeoBlockRenderer<T extends BlockEntity & GeoAnimatable> implements GeoRenderer<T>, BlockEntityRenderer<T> {
	protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);
	protected final GeoModel<T> model;

	protected T animatable;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	protected Matrix4f blockRenderTranslations = new Matrix4f();
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	public GeoBlockRenderer(GeoModel<T> model) {
		this.model = model;
	}

	
	@Override
	public GeoModel<T> getGeoModel() {
		return this.model;
	}

	
	@Override
	public T getAnimatable() {
		return this.animatable;
	}

	
	@Override
	public long getInstanceId(T animatable) {
		return animatable.getBlockPos().hashCode();
	}

	
	@Override
	public List<GeoRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	
	public GeoBlockRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
		this.renderLayers.addLayer(renderLayer);

		return this;
	}

	
	public GeoBlockRenderer<T> withScale(float scale) {
		return withScale(scale, scale);
	}

	
	public GeoBlockRenderer<T> withScale(float scaleWidth, float scaleHeight) {
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;

		return this;
	}

	
	@Override
	public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		this.blockRenderTranslations = new Matrix4f(poseStack.last().pose());

		if (!isReRender)
			poseStack.translate(0.5, 0, 0.5);

		scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
	}

	@Override
	@ApiStatus.Internal
	public void render(T animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,
			int packedLight, int packedOverlay) {
		this.animatable = animatable;

		defaultRender(poseStack, this.animatable, bufferSource, null, null, 0, partialTick, packedLight);
	}

	
	@Override
	public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType,
							   MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
							   int packedOverlay, int colour) {
		if (!isReRender) {
			AnimationState<T> animationState = new AnimationState<T>(animatable, 0, 0, partialTick, false);
			long instanceId = getInstanceId(animatable);
			GeoModel<T> currentModel = getGeoModel();

			animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
			animationState.setData(DataTickets.BLOCK_ENTITY, animatable);
			currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
			rotateBlock(getFacing(animatable), poseStack);
			currentModel.handleAnimations(animatable, instanceId, animationState, partialTick);
		}

		this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

		if (buffer != null)
			GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick,
					packedLight, packedOverlay, colour);
	}

	
	@Override
	public void doPostRenderCleanup() {
		this.animatable = null;
	}

	
	@Override
	public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
								  int packedOverlay, int colour) {
		if (bone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(poseStack.last().pose());
			Matrix4f localMatrix = RenderUtil.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
			Matrix4f worldState = new Matrix4f(localMatrix);
			BlockPos pos = this.animatable.getBlockPos();

			bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			bone.setLocalSpaceMatrix(localMatrix);
			bone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
		}

		GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,
				colour);
	}

	
	protected void rotateBlock(Direction facing, PoseStack poseStack) {
		switch (facing) {
			case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
			case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
			case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
			case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
			case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
			case DOWN -> poseStack.mulPose(Axis.XN.rotationDegrees(90));
		}
	}

	
	protected Direction getFacing(T block) {
		BlockState blockState = block.getBlockState();

		if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
			return blockState.getValue(HorizontalDirectionalBlock.FACING);

		if (blockState.hasProperty(DirectionalBlock.FACING))
			return blockState.getValue(DirectionalBlock.FACING);

		return Direction.NORTH;
	}

	
	@Override
	public void updateAnimatedTextureFrame(T animatable) {
		AnimatableTexture.setAndUpdate(getTextureLocation(animatable));
	}

	
	@Override
	public void fireCompileRenderLayersEvent() {
		GeckoLibServices.Client.EVENTS.fireCompileBlockRenderLayers(this);
	}

	
	@Override
	public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
		return GeckoLibServices.Client.EVENTS.fireBlockPreRender(this, poseStack, model, bufferSource, partialTick, packedLight);
	}

	
	@Override
	public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
		GeckoLibServices.Client.EVENTS.fireBlockPostRender(this, poseStack, model, bufferSource, partialTick, packedLight);
	}
}
