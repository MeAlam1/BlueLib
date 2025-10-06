/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation.keyframe;

import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.cache.animation.EasingCache;

import java.util.List;
import java.util.Objects;

public record KeyframeCacheData(
		@Nullable List<MoLangValue> arrayData,
		@Nullable List<MoLangValue> pre,
		@Nullable List<MoLangValue> post,
		@Nullable EasingCache easing,
		@Nullable List<MoLangValue> easingArgs) {

	@Override
	public int hashCode() {
		return Objects.hash(this.arrayData, this.pre, this.post, this.easing, this.easingArgs);
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
		if (this == pObj)
			return true;

		if (pObj == null || getClass() != pObj.getClass())
			return false;

		return hashCode() == pObj.hashCode();
	}
}
