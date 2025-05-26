package software.bluelib.loader.renderer.layer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.renderer.GeoRenderer;

import java.util.List;


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
