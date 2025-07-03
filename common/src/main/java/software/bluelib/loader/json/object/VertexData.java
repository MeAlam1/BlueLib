/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.object;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record VertexData(@NotNull Vector3f position, @NotNull Float texU, @NotNull Float texV) {

	public VertexData(@NotNull Double pX, @NotNull Double pY, @NotNull Double pZ) {
		this(new Vector3f(pX.floatValue(), pY.floatValue(), pZ.floatValue()), 0F, 0F);
	}

	public VertexData withUVs(@NotNull Float pTexU, @NotNull Float pTexV) {
		return new VertexData(this.position, pTexU, pTexV);
	}
}
