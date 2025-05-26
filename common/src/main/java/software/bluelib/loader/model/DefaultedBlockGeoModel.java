package software.bluelib.loader.model;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.loader.animatable.GeoAnimatable;


public class DefaultedBlockGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {
	
	public DefaultedBlockGeoModel(ResourceLocation assetSubpath) {
		super(assetSubpath);
	}

	
	@Override
	protected String subtype() {
		return "block";
	}

	
	@Override
	public DefaultedBlockGeoModel<T> withAltModel(ResourceLocation altPath) {
		return (DefaultedBlockGeoModel<T>)super.withAltModel(altPath);
	}

	
	@Override
	public DefaultedBlockGeoModel<T> withAltAnimations(ResourceLocation altPath) {
		return (DefaultedBlockGeoModel<T>)super.withAltAnimations(altPath);
	}

	
	@Override
	public DefaultedBlockGeoModel<T> withAltTexture(ResourceLocation altPath) {
		return (DefaultedBlockGeoModel<T>)super.withAltTexture(altPath);
	}
}
