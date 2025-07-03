/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.object;

import org.joml.Vector3f;

public record VertexData(Vector3f position, float texU, float texV) {

	public VertexData(double pX, double pY, double pZ) {
		this(new Vector3f((float) pX, (float) pY, (float) pZ), 0, 0);
	}

	public VertexData withUVs(float pTexU, float pTexV) {
		return new VertexData(this.position, pTexU, pTexV);
	}
}
