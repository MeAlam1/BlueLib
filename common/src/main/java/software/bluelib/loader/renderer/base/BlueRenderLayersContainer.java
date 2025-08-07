/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.base;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;

public class BlueRenderLayersContainer<T extends BlueAnimatable> {

	@NotNull
	private final BlueRenderer<T> renderer;
	@NotNull
	private final List<BlueRenderLayer<T>> layers = new ObjectArrayList<>();
	private boolean compiledLayers = false;

	public BlueRenderLayersContainer(@NotNull BlueRenderer<T> pRenderer) {
		this.renderer = pRenderer;
	}

	@NotNull
	public List<BlueRenderLayer<T>> getRenderLayers() {
		if (!this.compiledLayers)
			fireCompileRenderLayersEvent();

		return this.layers;
	}

	public void addLayer(@NotNull BlueRenderLayer<T> pLayer) {
		this.layers.add(pLayer);
	}

	public void fireCompileRenderLayersEvent() {
		this.compiledLayers = true;

		this.renderer.fireCompileRenderLayersEvent();
	}
}
