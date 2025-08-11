/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.context;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

@SuppressWarnings("unused")
public final class BaseRenderContext<T extends BlueAnimatable> implements IRenderContext<T> {

	@NotNull
	private PoseStack poseStack;
	@NotNull
	private T animatable;
	@NotNull
	private ModelCache model;
	@NotNull
	private MultiBufferSource bufferSource;
	private boolean isReRender;
	private float partialTick;
	private int packedLight;
	private int packedOverlay;
	private int color;

	public BaseRenderContext(@NotNull PoseStack pPoseStack, @NotNull T pAnimatable, @NotNull ModelCache pModel,
			@NotNull MultiBufferSource pBufferSource, boolean pIsReRender, float pPartialTick,
			int pPackedLight, int pPackedOverlay, int pColor) {
		this.poseStack = pPoseStack;
		this.animatable = pAnimatable;
		this.model = pModel;
		this.bufferSource = pBufferSource;
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

	@NotNull
	public MultiBufferSource bufferSource() {
		return bufferSource;
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

	public void setBufferSource(@NotNull MultiBufferSource pBufferSource) {
		this.bufferSource = pBufferSource;
	}

	public void setReRender(boolean pReRender) {
		isReRender = pReRender;
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
