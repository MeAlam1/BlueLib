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
import software.bluelib.loader.animatable.base.BlueAnimatable;

public class BlueRenderLayersContainer<T extends BlueAnimatable> {

	private final BlueRenderer<T> renderer;
	private final List<BlueRenderLayer<T>> layers = new ObjectArrayList<>();
	private boolean compiledLayers = false;

	public BlueRenderLayersContainer(BlueRenderer<T> pRenderer) {
		this.renderer = pRenderer;
	}

	public List<BlueRenderLayer<T>> getRenderLayers() {
		if (!this.compiledLayers)
			fireCompileRenderLayersEvent();

		return this.layers;
	}

	public void addLayer(BlueRenderLayer<T> pLayer) {
		this.layers.add(pLayer);
	}

	public void fireCompileRenderLayersEvent() {
		this.compiledLayers = true;

		this.renderer.fireCompileRenderLayersEvent();
	}
}
