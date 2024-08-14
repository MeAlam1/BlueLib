// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.bluelib.entity.variant.base.ParameterBase;

import java.util.Map;
import java.util.Set;

public class VariantParameter extends ParameterBase {
    private final String jsonKey;

    public VariantParameter(String pJsonKey, JsonObject pJsonObject) {
        this.jsonKey = pJsonKey;
        Set<Map.Entry<String, JsonElement>> entrySet = pJsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            addParameter(entry.getKey(), entry.getValue().getAsString());
        }
    }

    public String getEntityName() {
        return this.jsonKey;
    }

    public String getVariantName() {
        return (String) getParameter("VariantName");
    }

    public String getParameter(String pKey) {
        return (String) super.getParameter(pKey);
    }
}
