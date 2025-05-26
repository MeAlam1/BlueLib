/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache.model;

import net.minecraft.world.phys.Vec3;
import software.bluelib.client.loader.json.model.object.QuadData;

import java.util.List;

public record CubeCache(List<QuadData> quads, Vec3 pivot, Vec3 rotation, Vec3 size, double inflate, boolean mirror) {}
