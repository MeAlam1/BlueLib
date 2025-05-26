package software.bluelib.loader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoRenderer;


public abstract class GeoRenderLayer<T extends GeoAnimatable> {
	protected final GeoRenderer<T> renderer;

	public GeoRenderLayer(GeoRenderer<T> entityRendererIn) {
		this.renderer = entityRendererIn;
	}

	
	public GeoModel<T> getGeoModel() {
		return this.renderer.getGeoModel();
	}

	
	public BakedGeoModel getDefaultBakedModel(T animatable) {
		return getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable, getRenderer()));
	}

	
	public GeoRenderer<T> getRenderer() {
		return this.renderer;
	}

	
	protected ResourceLocation getTextureResource(T animatable) {
		return getRenderer().getTextureLocation(animatable);
	}

	
	public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType,
						  MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick,
						  int packedLight, int packedOverlay) {}

	
	public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType,
								MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick,
								int packedLight, int packedOverlay) {}

	
	public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
							  MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {}
}