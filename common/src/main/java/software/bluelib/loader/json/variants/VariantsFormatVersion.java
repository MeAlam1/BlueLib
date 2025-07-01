/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.variants;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.json.FormatVersion;

public class VariantsFormatVersion extends FormatVersion<VariantsFormatVersion> {

    @NotNull
    public static final Registry<VariantsFormatVersion> REGISTRY = new Registry<>() {

        @NotNull
        private final Map<String, VariantsFormatVersion> map = new Object2ObjectOpenHashMap<>();

        @NotNull
        private final VariantsFormatVersion defaultVersion = new VariantsFormatVersion("1.0.0", true, null);

        {
            register(defaultVersion);
        }

        @NotNull
        @Override
        public Map<String, VariantsFormatVersion> versions() {
            return map;
        }

        @NotNull
        @Override
        public VariantsFormatVersion defaultVersion() {
            return defaultVersion;
        }
    };

    protected VariantsFormatVersion(@NotNull String pSerializedName, @NotNull Boolean pSupported, @Nullable String pErrorMessage) {
        super(pSerializedName, pSupported, pErrorMessage);
    }
}
