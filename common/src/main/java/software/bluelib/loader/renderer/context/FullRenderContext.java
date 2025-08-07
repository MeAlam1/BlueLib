/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.context;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

@SuppressWarnings("unused")
public final class FullRenderContext<T extends BlueAnimatable> implements IRenderContext<T> {

	@NotNull
	private PoseStack poseStack;
	@NotNull
	private T animatable;
	@NotNull
	private ModelCache model;
	@Nullable
	private RenderType renderType;
	@NotNull
	private MultiBufferSource bufferSource;
	@Nullable
	private VertexConsumer buffer;
	private boolean isReRender;
	private float partialTick;
	private int packedLight;
	private int packedOverlay;
	private int color;

	public FullRenderContext(@NotNull PoseStack pPoseStack, @NotNull T pAnimatable, @NotNull ModelCache pModel,
			@Nullable RenderType pRenderType, @NotNull MultiBufferSource pBufferSource,
			@Nullable VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick,
			int pPackedLight, int pPackedOverlay, int pColor) {
		this.poseStack = pPoseStack;
		this.animatable = pAnimatable;
		this.model = pModel;
		this.renderType = pRenderType;
		this.bufferSource = pBufferSource;
		this.buffer = pBuffer;
		this.isReRender = pIsReRender;
		this.partialTick = pPartialTick;
		this.packedLight = pPackedLight;
		this.packedOverlay = pPackedOverlay;
		this.color = pColor;
	}

	@NotNull
	public PoseStack poseStack() {
		return poseStack;
	}

	@NotNull
	public T animatable() {
		return animatable;
	}

	@NotNull
	public ModelCache model() {
		return model;
	}

	@Nullable
	public RenderType renderType() {
		return renderType;
	}

	@NotNull
	public MultiBufferSource bufferSource() {
		return bufferSource;
	}

	@Nullable
	public VertexConsumer buffer() {
		return buffer;
	}

	public boolean isReRender() {
		return isReRender;
	}

	public float partialTick() {
		return partialTick;
	}

	public int packedLight() {
		return packedLight;
	}

	public int packedOverlay() {
		return packedOverlay;
	}

	public int color() {
		return color;
	}

	public void setPoseStack(@NotNull PoseStack pPoseStack) {
		this.poseStack = pPoseStack;
	}

	public void setAnimatable(@NotNull T pAnimatable) {
		this.animatable = pAnimatable;
	}

	public void setModel(@NotNull ModelCache pModel) {
		this.model = pModel;
	}

	public void setRenderType(@Nullable RenderType pRenderType) {
		this.renderType = pRenderType;
	}

	public void setBufferSource(@NotNull MultiBufferSource pBufferSource) {
		this.bufferSource = pBufferSource;
	}

	public void setBuffer(@Nullable VertexConsumer pBuffer) {
		this.buffer = pBuffer;
	}

	public void setReRender(boolean pReRender) {
		this.isReRender = pReRender;
	}

	public void setPartialTick(float pPartialTick) {
		this.partialTick = pPartialTick;
	}

	public void setPackedLight(int pPackedLight) {
		this.packedLight = pPackedLight;
	}

	public void setPackedOverlay(int pPackedOverlay) {
		this.packedOverlay = pPackedOverlay;
	}

	public void setColor(int pColor) {
		this.color = pColor;
	}
}
