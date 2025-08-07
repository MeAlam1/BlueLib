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
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

@SuppressWarnings("unused")
public final class BaseRenderContext<T extends BlueAnimatable> implements IRenderContext<T> {

	private PoseStack poseStack;
	private T animatable;
	private ModelCache model;
	private MultiBufferSource bufferSource;
	private boolean isReRender;
	private float partialTick;
	private int packedLight;
	private int packedOverlay;
	private int color;

	public BaseRenderContext(PoseStack pPoseStack, T pAnimatable, ModelCache pModel,
			MultiBufferSource pBufferSource, boolean pIsReRender, float pPartialTick,
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

	public PoseStack poseStack() {
		return poseStack;
	}

	public T animatable() {
		return animatable;
	}

	public ModelCache model() {
		return model;
	}

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

	public void setPoseStack(PoseStack pPoseStack) {
		this.poseStack = pPoseStack;
	}

	public void setAnimatable(T pAnimatable) {
		this.animatable = pAnimatable;
	}

	public void setModel(ModelCache pModel) {
		this.model = pModel;
	}

	public void setBufferSource(MultiBufferSource pBufferSource) {
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
