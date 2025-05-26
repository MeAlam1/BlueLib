package software.bluelib.loader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.renderer.GeoRenderer;


public class BoneFilterGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
	protected final TriConsumer<GeoBone, T, Float> checkAndApply;

	public BoneFilterGeoLayer(GeoRenderer<T> renderer) {
		this(renderer, (bone, animatable, partialTick) -> {});
	}

	public BoneFilterGeoLayer(GeoRenderer<T> renderer, TriConsumer<GeoBone, T, Float> checkAndApply) {
		super(renderer);

		this.checkAndApply = checkAndApply;
	}

	
	protected void checkAndApply(GeoBone bone, T animatable, float partialTick) {
		this.checkAndApply.accept(bone, animatable, partialTick);
	}

	
	@Override
	public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		for (GeoBone bone : bakedModel.topLevelBones()) {
			checkChildBones(bone, animatable, partialTick);
		}
	}

	private void checkChildBones(GeoBone parentBone, T animatable, float partialTick) {
		checkAndApply(parentBone, animatable, partialTick);

		for (GeoBone bone : parentBone.getChildBones()) {
			checkChildBones(bone, animatable, partialTick);
		}
	}
}
