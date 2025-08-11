/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.model;

import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.json.object.QuadData;

public record CubeCache(@NotNull List<QuadData> quads, @NotNull Vec3 pivot, @NotNull Vec3 rotation, @NotNull Vec3 size,
		@NotNull Double inflate, boolean mirror) {}
