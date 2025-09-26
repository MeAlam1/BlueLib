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

public class ParticleKeyframeData extends KeyFrameData {

	@NotNull
	private final String effect;
	@NotNull
	private final String locator;
	@NotNull
	private final String script;

	public ParticleKeyframeData(double pStartTick, @NotNull String pEffect, @NotNull String pLocator, @NotNull String pScript) {
		super(pStartTick);

		this.script = pScript;
		this.locator = pLocator;
		this.effect = pEffect;
	}

	@NotNull
	public String getEffect() {
		return this.effect;
	}

	@NotNull
	public String getLocator() {
		return this.locator;
	}

	@NotNull
	public String script() {
		return this.script;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), effect, locator, script);
	}
}
