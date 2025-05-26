package software.bluelib.loader.animatable.instance;

import com.google.common.base.Suppliers;
import org.apache.commons.lang3.mutable.MutableObject;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.SingletonGeoAnimatable;
import software.bluelib.loader.animatable.client.GeoRenderProvider;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.constant.dataticket.DataTicket;

import java.util.function.Supplier;


public abstract class AnimatableInstanceCache {
	protected final GeoAnimatable animatable;
	protected final Supplier<GeoRenderProvider> renderProvider;

	public AnimatableInstanceCache(GeoAnimatable animatable) {
		this.animatable = animatable;
		this.renderProvider = Suppliers.memoize(() -> {
			if (!(this.animatable instanceof SingletonGeoAnimatable singleton) || !GeckoLibServices.PLATFORM.isPhysicalClient())
				return null;

			final MutableObject<GeoRenderProvider> consumer = new MutableObject<>(GeoRenderProvider.DEFAULT);

			singleton.createGeoRenderer(consumer::setValue);

			return consumer.getValue();
		});
	}

	
	public abstract <T extends GeoAnimatable> AnimatableManager<T> getManagerForId(long uniqueId);

	
	public <D> void addDataPoint(long uniqueId, DataTicket<D> dataTicket, D data) {
		getManagerForId(uniqueId).setData(dataTicket, data);
	}

	
	public <D> D getDataPoint(long uniqueId, DataTicket<D> dataTicket) {
		return getManagerForId(uniqueId).getData(dataTicket);
	}

	
	public Object getRenderProvider() {
		return this.renderProvider.get();
	}
}
