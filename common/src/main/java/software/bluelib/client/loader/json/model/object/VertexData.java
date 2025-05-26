/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model.object;

import org.joml.Vector3f;

public record VertexData(Vector3f position, float texU, float texV) {

    public VertexData(double x, double y, double z) {
        this(new Vector3f((float) x, (float) y, (float) z), 0, 0);
    }

    public VertexData withUVs(float texU, float texV) {
        return new VertexData(this.position, texU, texV);
    }
}
