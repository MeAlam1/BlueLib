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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animatable.base.SingletonBlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;
import software.bluelib.loader.renderer.client.BlueRenderProvider;

public abstract class AnimatableInstanceCache<T extends BlueAnimatable> {

	@NotNull
	protected final BlueAnimatable animatable;
	@Nullable
	protected final Supplier<BlueRenderProvider> renderProvider;

	public AnimatableInstanceCache(@NotNull BlueAnimatable pAnimatable) {
		this.animatable = pAnimatable;
		this.renderProvider = Suppliers.memoize(() -> {
			if (!(this.animatable instanceof SingletonBlueAnimatable singleton) || !BlueLibConstants.PlatformHelper.PLATFORM.isPhysicalClient())
				return null;

			final MutableObject<BlueRenderProvider> consumer = new MutableObject<>(BlueRenderProvider.DEFAULT);

			singleton.createBlueRenderer(consumer::setValue);

			return consumer.getValue();
		});
	}

	@NotNull
	public abstract <M extends BlueAnimatable> AnimatableManager<M> getManagerForId(long pUniqueId);

	public <D> void addDataPoint(long pUniqueId, @NotNull DataTicket<D> pDataTicket, @NotNull D pData) {
		getManagerForId(pUniqueId).setData(pDataTicket, pData);
	}

	@Nullable
	public <D> D getDataPoint(long pUniqueId, @NotNull DataTicket<D> pDataTicket) {
		return getManagerForId(pUniqueId).getData(pDataTicket);
	}

	@Nullable
	public Object getRenderProvider() {
		if (this.renderProvider == null) {
			return null;
		}
		return this.renderProvider.get();
	}
}
