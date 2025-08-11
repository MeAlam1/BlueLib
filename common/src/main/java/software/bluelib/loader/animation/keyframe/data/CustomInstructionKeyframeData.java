/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.data;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class CustomInstructionKeyframeData extends KeyFrameData {

	@NotNull
	private final String instructions;

	public CustomInstructionKeyframeData(double pStartTick, @NotNull String pInstructions) {
		super(pStartTick);

		this.instructions = pInstructions;
	}

	@NotNull
	public String getInstructions() {
		return this.instructions;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), instructions);
	}
}
