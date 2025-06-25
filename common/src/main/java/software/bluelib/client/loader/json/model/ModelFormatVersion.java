/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.loader.json.FormatVersion;

public class ModelFormatVersion extends FormatVersion<ModelFormatVersion> {

    private static final Map<String, ModelFormatVersion> REGISTRY = new Object2ObjectOpenHashMap<>();

    static {
        register("1.12.0");
        register("1.14.0");
        register("1.21.0");
    }

    protected ModelFormatVersion(String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
        super(pSerializedName, pSupported, pErrorMessage);
    }

    protected static ModelFormatVersion register(String pName) {
        return FormatVersion.register(REGISTRY, new ModelFormatVersion(pName, true, null));
    }

    protected static ModelFormatVersion register(String pName, boolean pSupported, @Nullable String pErrorMessage) {
        return FormatVersion.register(REGISTRY, new ModelFormatVersion(pName, pSupported, pErrorMessage));
    }

    @Nullable
    public static ModelFormatVersion match(String pVersion) {
        return FormatVersion.match(REGISTRY, pVersion);
    }

    public static Map<String, ModelFormatVersion> getRegisteredVersions() {
        return FormatVersion.getRegisteredVersions(REGISTRY);
    }
}
