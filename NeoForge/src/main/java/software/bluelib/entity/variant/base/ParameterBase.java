// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant.base;

import java.util.HashMap;
import java.util.Map;

public abstract class ParameterBase {
    private final Map<String, Object> parameters = new HashMap<>();

    protected void addParameter(String pKey, Object pValue) {
        parameters.put(pKey, pValue);
    }

    public Object getParameter(String pKey) {
        return parameters.get(pKey);
    }

    public void removeParameter(String pKey) {
        parameters.remove(pKey);
    }

    public Map<String, Object> getAllParameters() {
        return new HashMap<>(parameters);
    }
}
