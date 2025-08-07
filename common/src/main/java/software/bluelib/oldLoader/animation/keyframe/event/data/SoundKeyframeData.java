/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation.keyframe.event.data;

import java.util.Objects;

public class SoundKeyframeData extends KeyFrameData {

	private final String sound;

	public SoundKeyframeData(Double startTick, String sound) {
		super(startTick);

		this.sound = sound;
	}

	public String getSound() {
		return this.sound;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), this.sound);
	}
}
