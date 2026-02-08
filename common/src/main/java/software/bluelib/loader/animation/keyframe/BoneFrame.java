/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.model.BoneCache;

public class BoneFrame {

	@NotNull
	private final BoneCache bone;

	private float scaleX;
	private float scaleY;
	private float scaleZ;

	private float offsetPosX;
	private float offsetPosY;
	private float offsetPosZ;

	private float rotX;
	private float rotY;
	private float rotZ;

	private double lastResetRotationTick = 0;
	private double lastResetPositionTick = 0;
	private double lastResetScaleTick = 0;

	private boolean rotAnimInProgress = true;
	private boolean posAnimInProgress = true;
	private boolean scaleAnimInProgress = true;

	public BoneFrame(@NotNull BoneCache pBone) {
		this.rotX = pBone.getRotX();
		this.rotY = pBone.getRotY();
		this.rotZ = pBone.getRotZ();

		this.offsetPosX = pBone.getPosX();
		this.offsetPosY = pBone.getPosY();
		this.offsetPosZ = pBone.getPosZ();

		this.scaleX = pBone.getScaleX();
		this.scaleY = pBone.getScaleY();
		this.scaleZ = pBone.getScaleZ();

		this.bone = pBone;
	}

	@NotNull
	public static BoneFrame copy(@NotNull BoneFrame pSnapshot) {
		BoneFrame newSnapshot = new BoneFrame(pSnapshot.bone);

		newSnapshot.scaleX = pSnapshot.scaleX;
		newSnapshot.scaleY = pSnapshot.scaleY;
		newSnapshot.scaleZ = pSnapshot.scaleZ;

		newSnapshot.offsetPosX = pSnapshot.offsetPosX;
		newSnapshot.offsetPosY = pSnapshot.offsetPosY;
		newSnapshot.offsetPosZ = pSnapshot.offsetPosZ;

		newSnapshot.rotX = pSnapshot.rotX;
		newSnapshot.rotY = pSnapshot.rotY;
		newSnapshot.rotZ = pSnapshot.rotZ;

		return newSnapshot;
	}

	@NotNull
	public BoneCache getBone() {
		return this.bone;
	}

	public float getScaleX() {
		return this.scaleX;
	}

	public float getScaleY() {
		return this.scaleY;
	}

	public float getScaleZ() {
		return this.scaleZ;
	}

	public float getOffsetX() {
		return this.offsetPosX;
	}

	public float getOffsetY() {
		return this.offsetPosY;
	}

	public float getOffsetZ() {
		return this.offsetPosZ;
	}

	public float getRotX() {
		return this.rotX;
	}

	public float getRotY() {
		return this.rotY;
	}

	public float getRotZ() {
		return this.rotZ;
	}

	public double getLastResetRotationTick() {
		return this.lastResetRotationTick;
	}

	public double getLastResetPositionTick() {
		return this.lastResetPositionTick;
	}

	public double getLastResetScaleTick() {
		return this.lastResetScaleTick;
	}

	public boolean isRotAnimInProgress() {
		return this.rotAnimInProgress;
	}

	public boolean isPosAnimInProgress() {
		return this.posAnimInProgress;
	}

	public boolean isScaleAnimInProgress() {
		return this.scaleAnimInProgress;
	}

	public void updateScale(float pScaleX, float pScaleY, float pScaleZ) {
		this.scaleX = pScaleX;
		this.scaleY = pScaleY;
		this.scaleZ = pScaleZ;
	}

	public void updateOffset(float pOffsetX, float pOffsetY, float pOffsetZ) {
		this.offsetPosX = pOffsetX;
		this.offsetPosY = pOffsetY;
		this.offsetPosZ = pOffsetZ;
	}

	public void updateRotation(float pRotX, float pRotY, float pRotZ) {
		this.rotX = pRotX;
		this.rotY = pRotY;
		this.rotZ = pRotZ;
	}

	public void startPosAnim() {
		this.posAnimInProgress = true;
	}

	public void stopPosAnim(double pTick) {
		this.posAnimInProgress = false;
		this.lastResetPositionTick = pTick;
	}

	public void startRotAnim() {
		this.rotAnimInProgress = true;
	}

	public void stopRotAnim(double pTick) {
		this.rotAnimInProgress = false;
		this.lastResetRotationTick = pTick;
	}

	public void startScaleAnim() {
		this.scaleAnimInProgress = true;
	}

	public void stopScaleAnim(double pTick) {
		this.scaleAnimInProgress = false;
		this.lastResetScaleTick = pTick;
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
		if (this == pObj)
			return true;

		if (pObj == null || getClass() != pObj.getClass())
			return false;

		return hashCode() == pObj.hashCode();
	}

	@Override
	public int hashCode() {
		return this.bone.getName().hashCode();
	}
}
