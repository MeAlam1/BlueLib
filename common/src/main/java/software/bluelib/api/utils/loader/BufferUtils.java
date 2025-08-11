/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.loader;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BufferUtils {

	private BufferUtils() {}

	@NotNull
	@ApiStatus.Internal
	public static VertexConsumer checkAndRefreshBuffer(boolean pIsReRender, @Nullable VertexConsumer pBuffer, @NotNull MultiBufferSource pBufferSource, @Nullable RenderType pRenderType) {
		if (pRenderType == null)
			return pBufferSource.getBuffer(RenderType.translucent()); // TODO: Find a better solution

		if (pBuffer == null)
			return pBufferSource.getBuffer(pRenderType);

		if (pIsReRender)
			return pBuffer;

		return switch (pBuffer) {
			case BufferBuilder builder when !builder.building -> pBufferSource.getBuffer(pRenderType);
			case OutlineBufferSource.EntityOutlineGenerator outlines when bufferNeedsRefresh(outlines.delegate()) -> new OutlineBufferSource.EntityOutlineGenerator(pBufferSource.getBuffer(pRenderType), outlines.color());
			case VertexMultiConsumer.Double pair when bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second) -> new VertexMultiConsumer.Double(bufferNeedsRefresh(pair.first) ? pBufferSource.getBuffer(pRenderType) : pair.first, bufferNeedsRefresh(pair.second) ? pBufferSource.getBuffer(pRenderType) : pair.second);
			default -> pBuffer;
		};
	}

	@ApiStatus.Internal
	public static boolean bufferNeedsRefresh(@NotNull VertexConsumer pBuffer) {
		return switch (pBuffer) {
			case BufferBuilder builder -> !builder.building;
			case OutlineBufferSource.EntityOutlineGenerator outlines -> bufferNeedsRefresh(outlines.delegate());
			case VertexMultiConsumer.Double pair -> bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second);
			default -> false;
		};
	}
}
