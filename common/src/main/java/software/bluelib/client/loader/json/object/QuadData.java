/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.object;

import java.util.List;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import software.bluelib.client.loader.json.deserialize.model.FaceUV;

public record QuadData(List<VertexData> vertices, Vector3f normal, Direction direction) {

	public static QuadData build(List<VertexData> pVertices, List<Float> pUvCoords, List<Float> pUvSize, FaceUV.Rotation pUvRotation, float pTextureWidth, float pTextureHeight, boolean pMirror, Direction pDirection) {
		return build(pVertices, pUvCoords.get(0), pUvCoords.get(1), pUvSize.get(0), pUvSize.get(1), pUvRotation, pTextureWidth, pTextureHeight, pMirror, pDirection);
	}

	public static QuadData build(List<VertexData> pVertices, float pU, float pV, float pUSize, float pVSize, FaceUV.Rotation pUvRotation, float pTextureWidth, float pTextureHeight, boolean pMirror, Direction pDirection) {
		float uWidth = (pU + pUSize) / pTextureWidth;
		float vHeight = (pV + pVSize) / pTextureHeight;
		pU /= pTextureWidth;
		pV /= pTextureHeight;
		Vector3f normal = pDirection.step();

		if (!pMirror) {
			float tempWidth = uWidth;
			uWidth = pU;
			pU = tempWidth;

		} else {
			normal.mul(-1, 1, 1);
		}

		List<Float> uvs = pUvRotation.rotateUvs(pU, pV, uWidth, vHeight);
		pVertices.set(0, pVertices.get(0).withUVs(uvs.get(0), uvs.get(1)));
		pVertices.set(1, pVertices.get(1).withUVs(uvs.get(2), uvs.get(3)));
		pVertices.set(2, pVertices.get(2).withUVs(uvs.get(4), uvs.get(5)));
		pVertices.set(3, pVertices.get(3).withUVs(uvs.get(6), uvs.get(7)));

		return new QuadData(pVertices, normal, pDirection);
	}
}
