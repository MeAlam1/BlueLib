/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.controller;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.loader.json.FormatVersion;

public class ControllerFormatVersion extends FormatVersion<ControllerFormatVersion> {

    public static final Registry<ControllerFormatVersion> REGISTRY = new Registry<>() {

        private final Map<String, ControllerFormatVersion> map = new Object2ObjectOpenHashMap<>();
        private final ControllerFormatVersion defaultVersion = new ControllerFormatVersion("1.0.0", true, null);

        {
            register(defaultVersion);
        }

        @Override
        public Map<String, ControllerFormatVersion> versions() {
            return map;
        }

        @Override
        public ControllerFormatVersion defaultVersion() {
            return defaultVersion;
        }
    };

    protected ControllerFormatVersion(String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
        super(pSerializedName, pSupported, pErrorMessage);
    }
}
