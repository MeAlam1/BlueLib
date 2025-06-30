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
