/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import software.bluelib.loader.animation.state.BoneSnapshot;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class BoneCache {

	private final BoneCache parent;
	private final String name;

	private final List<BoneCache> children = new ObjectArrayList<>();
	private final List<CubeCache> cubes = new ObjectArrayList<>();

	private final Boolean mirror;
	private final Float inflate;
	private final Boolean dontRender;
	private final Boolean reset;

	private BoneSnapshot initialSnapshot;

	private boolean hidden;
	private boolean childrenHidden = false;

	private float scaleX = 1;
	private float scaleY = 1;
	private float scaleZ = 1;

	private float positionX;
	private float positionY;
	private float positionZ;

	private float pivotX;
	private float pivotY;
	private float pivotZ;

	private float rotX;
	private float rotY;
	private float rotZ;

	private boolean positionChanged = false;
	private boolean rotationChanged = false;
	private boolean scaleChanged = false;
	private final Matrix4f modelSpaceMatrix = new Matrix4f();
	private final Matrix4f localSpaceMatrix = new Matrix4f();
	private final Matrix4f worldSpaceMatrix = new Matrix4f();
	private Matrix3f worldSpaceNormal = new Matrix3f();

	private boolean trackingMatrices;

	public BoneCache(@Nullable BoneCache pParent, String pName, Boolean pMirror, @Nullable Float pInflate, @Nullable Boolean pDontRender, @Nullable Boolean pReset) {
		this.parent = pParent;
		this.name = pName;
		this.mirror = pMirror;
		this.inflate = pInflate;
		this.dontRender = pDontRender;
		this.reset = pReset;
		this.trackingMatrices = false;
		this.hidden = this.dontRender == Boolean.TRUE;

		this.worldSpaceNormal.identity();
		this.worldSpaceMatrix.identity();
		this.localSpaceMatrix.identity();
		this.modelSpaceMatrix.identity();
	}

	public String getName() {
		return this.name;
	}

	public BoneCache getParent() {
		return this.parent;
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

	public float getPosX() {
		return this.positionX;
	}

	public float getPosY() {
		return this.positionY;
	}

	public float getPosZ() {
		return this.positionZ;
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

	public void setRotX(float pValue) {
		this.rotX = pValue;

		markRotationAsChanged();
	}

	public void setRotY(float pValue) {
		this.rotY = pValue;

		markRotationAsChanged();
	}

	public void setRotZ(float pValue) {
		this.rotZ = pValue;

		markRotationAsChanged();
	}

	public void updateRotation(float pXRot, float pYRot, float pZRot) {
		setRotX(pXRot);
		setRotY(pYRot);
		setRotZ(pZRot);
	}

	public void setPosX(float pValue) {
		this.positionX = pValue;

		markPositionAsChanged();
	}

	public void setPosY(float pValue) {
		this.positionY = pValue;

		markPositionAsChanged();
	}

	public void setPosZ(float pValue) {
		this.positionZ = pValue;

		markPositionAsChanged();
	}

	public void updatePosition(float pPosX, float pPosY, float pPosZ) {
		setPosX(pPosX);
		setPosY(pPosY);
		setPosZ(pPosZ);
	}

	public void setScaleX(float pValue) {
		this.scaleX = pValue;

		markScaleAsChanged();
	}

	public void setScaleY(float pValue) {
		this.scaleY = pValue;

		markScaleAsChanged();
	}

	public void setScaleZ(float pValue) {
		this.scaleZ = pValue;

		markScaleAsChanged();
	}

	public void updateScale(float pScaleX, float pScaleY, float pScaleZ) {
		setScaleX(pScaleX);
		setScaleY(pScaleY);
		setScaleZ(pScaleZ);
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public void setHidden(boolean pHidden) {
		this.hidden = pHidden;

		setChildrenHidden(pHidden);
	}

	public void setChildrenHidden(boolean pHideChildren) {
		this.childrenHidden = pHideChildren;
	}

	public void setPivotX(float pValue) {
		this.pivotX = pValue;
	}

	public void setPivotY(float pValue) {
		this.pivotY = pValue;
	}

	public void setPivotZ(float pValue) {
		this.pivotZ = pValue;
	}

	public void updatePivot(float pPivotX, float pPivotY, float pPivotZ) {
		setPivotX(pPivotX);
		setPivotY(pPivotY);
		setPivotZ(pPivotZ);
	}

	public float getPivotX() {
		return this.pivotX;
	}

	public float getPivotY() {
		return this.pivotY;
	}

	public float getPivotZ() {
		return this.pivotZ;
	}

	public boolean isHidingChildren() {
		return this.childrenHidden;
	}

	public void markScaleAsChanged() {
		this.scaleChanged = true;
	}

	public void markRotationAsChanged() {
		this.rotationChanged = true;
	}

	public void markPositionAsChanged() {
		this.positionChanged = true;
	}

	public boolean hasScaleChanged() {
		return this.scaleChanged;
	}

	public boolean hasRotationChanged() {
		return this.rotationChanged;
	}

	public boolean hasPositionChanged() {
		return this.positionChanged;
	}

	public void resetStateChanges() {
		this.scaleChanged = false;
		this.rotationChanged = false;
		this.positionChanged = false;
	}

	public BoneSnapshot getInitialSnapshot() {
		return this.initialSnapshot;
	}

	public List<BoneCache> getChildBones() {
		return this.children;
	}

	public void saveInitialSnapshot() {
		if (this.initialSnapshot == null)
			this.initialSnapshot = saveSnapshot();
	}

	public Boolean getMirror() {
		return this.mirror;
	}

	public Float getInflate() {
		return this.inflate;
	}

	public Boolean shouldNeverRender() {
		return this.dontRender;
	}

	public Boolean getReset() {
		return this.reset;
	}

	public List<CubeCache> getCubes() {
		return this.cubes;
	}

	public boolean isTrackingMatrices() {
		return trackingMatrices;
	}

	public void setTrackingMatrices(boolean pTrackingMatrices) {
		this.trackingMatrices = pTrackingMatrices;
	}

	public Matrix4f getModelSpaceMatrix() {
		setTrackingMatrices(true);

		return this.modelSpaceMatrix;
	}

	public void setModelSpaceMatrix(Matrix4f pMatrix) {
		this.modelSpaceMatrix.set(pMatrix);
	}

	public Matrix4f getLocalSpaceMatrix() {
		setTrackingMatrices(true);

		return this.localSpaceMatrix;
	}

	public void setLocalSpaceMatrix(Matrix4f pMatrix) {
		this.localSpaceMatrix.set(pMatrix);
	}

	public Matrix4f getWorldSpaceMatrix() {
		setTrackingMatrices(true);

		return this.worldSpaceMatrix;
	}

	public void setWorldSpaceMatrix(Matrix4f pMatrix) {
		this.worldSpaceMatrix.set(pMatrix);
	}

	public void setWorldSpaceNormal(Matrix3f pMatrix) {
		this.worldSpaceNormal = pMatrix;
	}

	public Matrix3f getWorldSpaceNormal() {
		return worldSpaceNormal;
	}

	public Vector3d getLocalPosition() {
		Vector4f vec = getLocalSpaceMatrix().transform(new Vector4f(0, 0, 0, 1));

		return new Vector3d(vec.x(), vec.y(), vec.z());
	}

	public Vector3d getModelPosition() {
		Vector4f vec = getModelSpaceMatrix().transform(new Vector4f(0, 0, 0, 1));

		return new Vector3d(-vec.x() * 16f, vec.y() * 16f, vec.z() * 16f);
	}

	public Vector3d getWorldPosition() {
		Vector4f vec = getWorldSpaceMatrix().transform(new Vector4f(0, 0, 0, 1));

		return new Vector3d(vec.x(), vec.y(), vec.z());
	}

	public void setModelPosition(Vector3d pPos) {
		// Doesn't work on bones with parent transforms
		BoneCache parent = getParent();
		Matrix4f matrix = (parent == null ? new Matrix4f().identity() : new Matrix4f(parent.getModelSpaceMatrix())).invert();
		Vector4f vec = matrix.transform(new Vector4f(-(float) pPos.x / 16f, (float) pPos.y / 16f, (float) pPos.z / 16f, 1));

		updatePosition(-vec.x() * 16f, vec.y() * 16f, vec.z() * 16f);
	}

	public Matrix4f getModelRotationMatrix() {
		Matrix4f matrix = new Matrix4f(getModelSpaceMatrix());
		matrix.m03(0);
		matrix.m13(0);
		matrix.m23(0);

		return matrix;
	}

	public Vector3d getPositionVector() {
		return new Vector3d(getPosX(), getPosY(), getPosZ());
	}

	public Vector3d getRotationVector() {
		return new Vector3d(getRotX(), getRotY(), getRotZ());
	}

	public Vector3d getScaleVector() {
		return new Vector3d(getScaleX(), getScaleY(), getScaleZ());
	}

	public void addRotationOffsetFromBone(BoneCache pSource) {
		setRotX(getRotX() + pSource.getRotX() - pSource.getInitialSnapshot().getRotX());
		setRotY(getRotY() + pSource.getRotY() - pSource.getInitialSnapshot().getRotY());
		setRotZ(getRotZ() + pSource.getRotZ() - pSource.getInitialSnapshot().getRotZ());
	}

	public BoneSnapshot saveSnapshot() {
		return new BoneSnapshot(this);
	}

	public boolean equals(Object pObj) {
		if (this == pObj)
			return true;

		if (pObj == null || getClass() != pObj.getClass())
			return false;

		return hashCode() == pObj.hashCode();
	}

	public int hashCode() {
		return Objects.hash(getName(), (getParent() != null ? getParent().getName() : 0), getCubes().size(), getChildBones().size());
	}
}
