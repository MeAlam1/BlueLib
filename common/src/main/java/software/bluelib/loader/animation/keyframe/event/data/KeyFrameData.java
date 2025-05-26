/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation.keyframe.event.data;

import software.bluelib.loader.animation.keyframe.Keyframe;

import java.util.Objects;


public abstract class KeyFrameData {
	private final double startTick;

	public KeyFrameData(double startTick) {
		this.startTick = startTick;
	}

	
	public double getStartTick() {
		return this.startTick;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		return this.hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.startTick);
	}
}
