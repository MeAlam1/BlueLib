/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.loader.json.FormatVersion;

public class AnimationFormatVersion extends FormatVersion<AnimationFormatVersion> {

    private static final Map<String, AnimationFormatVersion> REGISTRY = new Object2ObjectOpenHashMap<>();

    static {
        register("1.8.0");
    }

    protected AnimationFormatVersion(String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
        super(pSerializedName, pSupported, pErrorMessage);
    }

    protected static AnimationFormatVersion register(String pName) {
        return FormatVersion.register(REGISTRY, new AnimationFormatVersion(pName, true, null));
    }

    protected static AnimationFormatVersion register(String pName, boolean pSupported, @Nullable String pErrorMessage) {
        return FormatVersion.register(REGISTRY, new AnimationFormatVersion(pName, pSupported, pErrorMessage));
    }

    @Nullable
    public static AnimationFormatVersion match(@Nullable String pVersion) {
        return FormatVersion.match(REGISTRY, pVersion);
    }

    public static Map<String, AnimationFormatVersion> getRegisteredVersions() {
        return FormatVersion.getRegisteredVersions(REGISTRY);
    }
}
