/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json;

import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public abstract class FormatVersion<T extends FormatVersion<T>> {

    private final String serializedName;
    private final boolean supported;
    private final String errorMessage;

    protected FormatVersion(String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
        this.serializedName = pSerializedName;
        this.supported = pSupported;
        this.errorMessage = pErrorMessage;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public boolean isSupported() {
        return supported;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    protected static <T extends FormatVersion<T>> T register(Map<String, T> pRegistry, T pVersion) {
        pRegistry.put(pVersion.getSerializedName(), pVersion);
        return pVersion;
    }

    @Nullable
    protected static <T extends FormatVersion<T>> T match(Map<String, T> pRegistry, String pVersion) {
        return pRegistry.get(pVersion);
    }

    protected static <T extends FormatVersion<T>> Map<String, T> getRegisteredVersions(Map<String, T> pRegistry) {
        return Collections.unmodifiableMap(pRegistry);
    }
}
