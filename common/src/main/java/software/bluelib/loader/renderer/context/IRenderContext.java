package software.bluelib.loader.renderer.context;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

// TODO: Change all the BlueRenderer (And Inheritors) Methods to use this RenderContext instead of passing all the parameters individually.

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
