/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.renderer.BlueRenderer;

public class BlueRenderLayersContainer<T extends BlueAnimatable> {

    private final BlueRenderer<T> renderer;
    private final List<BlueRenderLayer<T>> layers = new ObjectArrayList<>();
    private boolean compiledLayers = false;

    public BlueRenderLayersContainer(BlueRenderer<T> renderer) {
        this.renderer = renderer;
    }

    public List<BlueRenderLayer<T>> getRenderLayers() {
        if (!this.compiledLayers)
            fireCompileRenderLayersEvent();

        return this.layers;
    }

    public void addLayer(BlueRenderLayer<T> layer) {
        this.layers.add(layer);
    }

    public void fireCompileRenderLayersEvent() {
        this.compiledLayers = true;

        this.renderer.fireCompileRenderLayersEvent();
    }
}
