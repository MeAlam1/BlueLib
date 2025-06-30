/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.variants;

import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public record EntityCache(
        String formatVersion,
        Map<String, VariantCache> variants) {

    public Set<String> getVariantNames() {
        return variants.keySet();
    }

    @Nullable
    public VariantCache getVariant(String pName) {
        return variants.get(pName);
    }
}
