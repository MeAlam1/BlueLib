package software.bluelib.loader.renderer.context;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

public record FullRenderContext<T extends BlueAnimatable>(
		PoseStack poseStack,
		T animatable,
		ModelCache model,
		RenderType renderType,
		MultiBufferSource bufferSource,
		VertexConsumer buffer,
		boolean isReRender,
		float partialTick,
		int packedLight,
		int packedOverlay,
		int color) implements IRenderContext<T> {}
