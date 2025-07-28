package software.bluelib.api.utils.loader;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.ApiStatus;

public final class BufferUtils {
	private BufferUtils() {
	}

	@ApiStatus.Internal
	public static VertexConsumer checkAndRefreshBuffer(boolean pIsReRender, VertexConsumer pBuffer, MultiBufferSource pBufferSource, RenderType pRenderType) {
		if (pIsReRender)
			return pBuffer;

		return switch (pBuffer) {
			case BufferBuilder builder when !builder.building -> pBufferSource.getBuffer(pRenderType);
			case OutlineBufferSource.EntityOutlineGenerator outlines when bufferNeedsRefresh(outlines.delegate()) ->
					new OutlineBufferSource.EntityOutlineGenerator(pBufferSource.getBuffer(pRenderType), outlines.color());
			case VertexMultiConsumer.Double pair when bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second) ->
					new VertexMultiConsumer.Double(bufferNeedsRefresh(pair.first) ? pBufferSource.getBuffer(pRenderType) : pair.first, bufferNeedsRefresh(pair.second) ? pBufferSource.getBuffer(pRenderType) : pair.second);
			default -> pBuffer;
		};
	}

	@ApiStatus.Internal
	public static boolean bufferNeedsRefresh(VertexConsumer pBuffer) {
		return switch (pBuffer) {
			case BufferBuilder builder -> !builder.building;
			case OutlineBufferSource.EntityOutlineGenerator outlines -> bufferNeedsRefresh(outlines.delegate());
			case VertexMultiConsumer.Double pair -> bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second);
			default -> false;
		};
	}
}