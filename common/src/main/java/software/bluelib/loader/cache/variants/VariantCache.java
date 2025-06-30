/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.variants;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public record VariantCache(
        @Nullable JsonArray parameters) {

    @Nullable
    public JsonElement getParameter(String pParameterName) {
        if (parameters == null) return null;
        for (JsonElement el : parameters) {
            if (el.isJsonObject() && el.getAsJsonObject().has(pParameterName)) {
                return el.getAsJsonObject().get(pParameterName);
            }
        }
        return null;
    }
}
