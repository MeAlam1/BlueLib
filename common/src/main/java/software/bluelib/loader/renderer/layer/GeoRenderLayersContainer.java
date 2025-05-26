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
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.renderer.GeoRenderer;

public class GeoRenderLayersContainer<T extends GeoAnimatable> {

    private final GeoRenderer<T> renderer;
    private final List<GeoRenderLayer<T>> layers = new ObjectArrayList<>();
    private boolean compiledLayers = false;

    public GeoRenderLayersContainer(GeoRenderer<T> renderer) {
        this.renderer = renderer;
    }

    public List<GeoRenderLayer<T>> getRenderLayers() {
        if (!this.compiledLayers)
            fireCompileRenderLayersEvent();

        return this.layers;
    }

    public void addLayer(GeoRenderLayer<T> layer) {
        this.layers.add(layer);
    }

    public void fireCompileRenderLayersEvent() {
        this.compiledLayers = true;

        this.renderer.fireCompileRenderLayersEvent();
    }
}
