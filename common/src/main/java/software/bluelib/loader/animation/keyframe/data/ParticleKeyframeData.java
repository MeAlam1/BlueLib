/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.data;

import java.util.Objects;

public class ParticleKeyframeData extends KeyFrameData {

	private final String effect;
	private final String locator;
	private final String script;

	public ParticleKeyframeData(double pStartTick, String pEffect, String pLocator, String pScript) {
		super(pStartTick);

		this.script = pScript;
		this.locator = pLocator;
		this.effect = pEffect;
	}

	public String getEffect() {
		return this.effect;
	}

	public String getLocator() {
		return this.locator;
	}

	public String script() {
		return this.script;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), effect, locator, script);
	}
}
