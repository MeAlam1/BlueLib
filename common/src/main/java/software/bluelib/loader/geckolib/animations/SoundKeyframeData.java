/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.animations;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class SoundKeyframeData extends KeyFrameData {

	@NotNull
	private final String sound;

	public SoundKeyframeData(@NotNull Double pStartTick, @NotNull String pSound) {
		super(pStartTick);

		this.sound = pSound;
	}

	@NotNull
	public String getSound() {
		return this.sound;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), this.sound);
	}
}
