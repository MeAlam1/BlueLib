package software.bluelib.loader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.texture.AutoGlowingTexture;
import software.bluelib.loader.renderer.GeoRenderer;
import software.bluelib.loader.util.ClientUtil;


public class AutoGlowingGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
	public AutoGlowingGeoLayer(GeoRenderer<T> renderer) {
		super(renderer);
	}

	
	@Deprecated(forRemoval = true)
	protected RenderType getRenderType(T animatable) {
		return getRenderType(animatable, null);
	}

	
	@Nullable
	protected RenderType getRenderType(T animatable, @Nullable MultiBufferSource bufferSource) {
		if (!(animatable instanceof Entity entity))
			return AutoGlowingTexture.getRenderType(getTextureResource(animatable));

		boolean invisible = entity.isInvisible();
		ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable));

		if (invisible && !entity.isInvisibleTo(ClientUtil.getClientPlayer()))
			return RenderType.itemEntityTranslucentCull(texture);

		if (Minecraft.getInstance().shouldEntityAppearGlowing(entity)) {
			if (invisible)
				return RenderType.outline(texture);

			return AutoGlowingTexture.getOutlineRenderType(getTextureResource(animatable));
		}

		return invisible ? null : AutoGlowingTexture.getRenderType(getTextureResource(animatable));
	}

	
	@Override
	public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		renderType = getRenderType(animatable);

		if (renderType != null) {
			getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
								   bufferSource.getBuffer(renderType), partialTick, LightTexture.FULL_SKY, packedOverlay,
					getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt());
		}
	}
}
