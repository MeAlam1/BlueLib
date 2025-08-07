/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.data;

import java.util.Objects;

public class CustomInstructionKeyframeData extends KeyFrameData {

	private final String instructions;

	public CustomInstructionKeyframeData(double startTick, String instructions) {
		super(startTick);

		this.instructions = instructions;
	}

	public String getInstructions() {
		return this.instructions;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), instructions);
	}
}
