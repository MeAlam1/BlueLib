/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;

public final class AnimationExtraData {

	@NotNull
	private final Map<DataTicket<?>, Object> data = new Object2ObjectOpenHashMap<>();

	@Nullable
	public <D> D get(@NotNull DataTicket<D> pTicket) {
		Objects.requireNonNull(pTicket, "Data ticket cannot be null");
		return pTicket.getData(this.data);
	}

	public <D> void set(@NotNull DataTicket<D> pTicket, @Nullable D pValue) {
		Objects.requireNonNull(pTicket, "Data ticket cannot be null");
		if (pValue == null) {
			this.data.remove(pTicket);
		} else {
			this.data.put(pTicket, pValue);
		}
	}

	public boolean has(@NotNull DataTicket<?> pTicket) {
		Objects.requireNonNull(pTicket, "Data ticket cannot be null");
		return this.data.containsKey(pTicket);
	}

	public void clear() {
		this.data.clear();
	}

	@NotNull
	public Map<DataTicket<?>, ?> asUnmodifiableMap() {
		return Collections.unmodifiableMap(this.data);
	}
}
