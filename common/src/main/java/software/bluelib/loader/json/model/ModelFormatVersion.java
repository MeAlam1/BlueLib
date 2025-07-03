/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.json.FormatVersion;

import java.util.Map;

public class ModelFormatVersion extends FormatVersion<ModelFormatVersion> {

	@NotNull
	public static final Registry<ModelFormatVersion> REGISTRY = new Registry<>() {

		@NotNull
		private final Map<String, ModelFormatVersion> map = new Object2ObjectOpenHashMap<>();
		@NotNull
		private final ModelFormatVersion defaultVersion = new ModelFormatVersion("1.12.0", true, null);

		{
			register(defaultVersion);
			register(new ModelFormatVersion("1.14.0", true, null));
			register(new ModelFormatVersion("1.21.0", true, null));
		}

		@Override
		public @NotNull Map<String, ModelFormatVersion> versions() {
			return map;
		}

		@Override
		public @NotNull ModelFormatVersion defaultVersion() {
			return defaultVersion;
		}
	};

	protected ModelFormatVersion(@NotNull String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
		super(pSerializedName, pSupported, pErrorMessage);
	}
}
