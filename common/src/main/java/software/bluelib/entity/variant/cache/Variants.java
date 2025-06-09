/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.entity.variant.cache;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

public record Variants(
        String entityName,
        String variantName,
        Map<String, JsonElement> parameters) {

    public Variants(String pEntityName, String pVariantName, JsonObject pJsonObject) {
        this(pEntityName, pVariantName, Map.copyOf(pJsonObject.entrySet().stream()
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll)));
    }

    public JsonElement getParameter(String pParameter) {
        return parameters.get(pParameter);
    }
}
