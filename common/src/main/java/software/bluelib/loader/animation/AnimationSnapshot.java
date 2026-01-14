/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

public record AnimationSnapshot(
		float limbSwing,
		float limbSwingAmount,
		float partialTick,
		boolean isMoving) {

	public AnimationSnapshot {
		if (Float.isNaN(limbSwing) || Float.isNaN(limbSwingAmount) || Float.isNaN(partialTick)) {
			throw new IllegalArgumentException("Animation values cannot be NaN");
		}
	}
}
