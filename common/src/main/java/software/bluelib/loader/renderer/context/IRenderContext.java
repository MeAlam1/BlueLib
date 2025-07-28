package software.bluelib.loader.renderer.context;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

public sealed interface IRenderContext<T extends BlueAnimatable>
		permits BaseRenderContext, FullRenderContext {

	PoseStack poseStack();

	T animatable();

	ModelCache model();

	MultiBufferSource bufferSource();

	boolean isReRender();

	float partialTick();

	int packedLight();

	int packedOverlay();

	int color();
}
