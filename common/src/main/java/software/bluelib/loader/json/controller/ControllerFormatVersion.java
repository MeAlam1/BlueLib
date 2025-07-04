/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.controller;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.json.FormatVersion;

public class ControllerFormatVersion extends FormatVersion<ControllerFormatVersion> {

	@NotNull
	public static final Registry<ControllerFormatVersion> REGISTRY = new Registry<>() {

		@NotNull
		private final Map<String, ControllerFormatVersion> map = new Object2ObjectOpenHashMap<>();
		@NotNull
		private final ControllerFormatVersion defaultVersion = new ControllerFormatVersion("1.0.0", true, null);

		{
			register(defaultVersion);
		}

		@Override
		public @NotNull Map<String, ControllerFormatVersion> versions() {
			return map;
		}

		@Override
		public @NotNull ControllerFormatVersion defaultVersion() {
			return defaultVersion;
		}
	};

	protected ControllerFormatVersion(@NotNull String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
		super(pSerializedName, pSupported, pErrorMessage);
	}
}
