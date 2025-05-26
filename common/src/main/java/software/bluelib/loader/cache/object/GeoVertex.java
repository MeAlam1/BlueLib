/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.object;

import org.joml.Vector3f;

public record GeoVertex(Vector3f position, float texU, float texV) {

    public GeoVertex(double x, double y, double z) {
        this(new Vector3f((float) x, (float) y, (float) z), 0, 0);
    }

    public GeoVertex withUVs(float texU, float texV) {
        return new GeoVertex(this.position, texU, texV);
    }
}
