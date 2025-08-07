/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.cache;

import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import org.apache.commons.lang3.mutable.MutableObject;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animatable.base.SingletonBlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;
import software.bluelib.loader.renderer.client.BlueRenderProvider;

public abstract class AnimatableInstanceCache<T extends BlueAnimatable> {

	protected final BlueAnimatable animatable;
	protected final Supplier<BlueRenderProvider> renderProvider;

	public AnimatableInstanceCache(BlueAnimatable pAnimatable) {
		this.animatable = pAnimatable;
		this.renderProvider = Suppliers.memoize(() -> {
			if (!(this.animatable instanceof SingletonBlueAnimatable singleton) || !BlueLibConstants.PlatformHelper.PLATFORM.isPhysicalClient())
				return null;

			final MutableObject<BlueRenderProvider> consumer = new MutableObject<>(BlueRenderProvider.DEFAULT);

			singleton.createBlueRenderer(consumer::setValue);

			return consumer.getValue();
		});
	}

	public abstract <M extends BlueAnimatable> AnimatableManager<M> getManagerForId(long pUniqueId);

	public <D> void addDataPoint(long pUniqueId, DataTicket<D> pDataTicket, D pData) {
		getManagerForId(pUniqueId).setData(pDataTicket, pData);
	}

	public <D> D getDataPoint(long pUniqueId, DataTicket<D> pDataTicket) {
		return getManagerForId(pUniqueId).getData(pDataTicket);
	}

	public Object getRenderProvider() {
		return this.renderProvider.get();
	}
}
