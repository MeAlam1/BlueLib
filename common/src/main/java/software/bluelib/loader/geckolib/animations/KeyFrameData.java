/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.animations;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public abstract class KeyFrameData {

	private final double startTick;

	public KeyFrameData(double pStartTick) {
		this.startTick = pStartTick;
	}

	public double getStartTick() {
		return this.startTick;
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
		if (this == pObj)
			return true;

		if (pObj == null || getClass() != pObj.getClass())
			return false;

		return this.hashCode() == pObj.hashCode();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.startTick);
	}
}
