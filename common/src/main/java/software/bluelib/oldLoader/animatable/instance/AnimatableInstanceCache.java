/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable.instance;

import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import org.apache.commons.lang3.mutable.MutableObject;
import software.bluelib.BlueLibConstants;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animatable.SingletonBlueAnimatable;
import software.bluelib.oldLoader.animatable.client.BlueRenderProvider;
import software.bluelib.oldLoader.animation.AnimatableManager;
import software.bluelib.oldLoader.constant.dataticket.DataTicket;

public abstract class AnimatableInstanceCache {

	protected final BlueAnimatable animatable;
	protected final Supplier<BlueRenderProvider> renderProvider;

	public AnimatableInstanceCache(BlueAnimatable animatable) {
		this.animatable = animatable;
		this.renderProvider = Suppliers.memoize(() -> {
			if (!(this.animatable instanceof SingletonBlueAnimatable singleton) || !BlueLibConstants.PlatformHelper.PLATFORM.isPhysicalClient())
				return null;

			final MutableObject<BlueRenderProvider> consumer = new MutableObject<>(BlueRenderProvider.DEFAULT);

			singleton.createBlueRenderer(consumer::setValue);

			return consumer.getValue();
		});
	}

	public abstract <T extends BlueAnimatable> AnimatableManager<T> getManagerForId(long uniqueId);

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
