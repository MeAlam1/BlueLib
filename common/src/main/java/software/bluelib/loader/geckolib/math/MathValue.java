/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.math;

import java.util.function.DoubleSupplier;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.annotations.WillBeDeprecated;

@WillBeDeprecated(since = "2.3.1", reason = "MathValue will be made redundant with the new MoLang System.")
public interface MathValue extends DoubleSupplier {

	double get();

	default boolean isMutable() {
		return true;
	}

	@ApiStatus.Internal
	@Override
	default double getAsDouble() {
		return get();
	}
}
