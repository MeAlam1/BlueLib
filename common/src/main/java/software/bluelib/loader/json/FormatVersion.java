/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json;

import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FormatVersion<T extends FormatVersion<T>> {

    private final String serializedName;
    private final boolean supported;
    private final String errorMessage;

    protected FormatVersion(@NotNull String pSerializedName, @NotNull Boolean pSupported, @Nullable String pErrorMessage) {
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

    @NotNull
    protected static <T extends FormatVersion<T>> T register(@NotNull Map<String, T> pRegistry, @NotNull T pVersion) {
        pRegistry.put(pVersion.getSerializedName(), pVersion);
        return pVersion;
    }

    public interface Registry<T extends FormatVersion<T>> {

        @NotNull
        Map<String, T> versions();

        @NotNull
        T defaultVersion();

        @NotNull
        default T get(@NotNull String pName) {
            return versions().getOrDefault(pName, defaultVersion());
        }

        default void register(@NotNull T pVersion) {
            versions().put(pVersion.getSerializedName(), pVersion);
        }

        @NotNull
        default Map<String, T> getRegisteredVersions() {
            return Collections.unmodifiableMap(versions());
        }

        @NotNull
        default T match(@NotNull String pVersion) {
            return get(pVersion);
        }
    }
}
