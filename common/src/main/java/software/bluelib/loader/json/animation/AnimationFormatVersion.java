/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.json.FormatVersion;

public class AnimationFormatVersion extends FormatVersion<AnimationFormatVersion> {

	@NotNull
	public static final Registry<AnimationFormatVersion> REGISTRY = new Registry<>() {

		@NotNull
		private final Map<String, AnimationFormatVersion> map = new Object2ObjectOpenHashMap<>();
		@NotNull
		private final AnimationFormatVersion defaultVersion = new AnimationFormatVersion("1.8.0", true, null);

		{
			register(defaultVersion);
		}

		@Override
		@NotNull
		public Map<String, AnimationFormatVersion> versions() {
			return map;
		}

		@Override
		@NotNull
		public AnimationFormatVersion defaultVersion() {
			return defaultVersion;
		}
	};

	protected AnimationFormatVersion(@NotNull String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
		super(pSerializedName, pSupported, pErrorMessage);
	}
}
