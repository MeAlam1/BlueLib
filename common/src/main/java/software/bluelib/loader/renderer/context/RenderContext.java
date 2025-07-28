package software.bluelib.loader.renderer.context;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

// TODO: Change all the BlueRenderer (And Inheritors) Methods to use this RenderContext instead of passing all the parameters individually.
public record RenderContext<T extends BlueAnimatable>(
		PoseStack poseStack,
		T animatable,
		ModelCache model,
		@Nullable RenderType renderType,
		MultiBufferSource bufferSource,
		@Nullable VertexConsumer buffer,
		boolean isReRender,
		float partialTick,
		int packedLight,
		int packedOverlay,
		int color
) {
}