package software.bluelib.entity.variant;

import java.util.HashMap;
import java.util.Map;

public abstract class VariantKeysBase {
    private final Map<String, Object> parameters = new HashMap<>();

    protected void AddParameter(String pKey, Object pValue) {
        parameters.put(pKey, pValue);
    }

    public Object GetParameter(String pKey) {
        return parameters.get(pKey);
    }

    public void RemoveParameter(String pKey) {
        parameters.remove(pKey);
    }

    public Map<String, Object> GetAllParameters() {
        return new HashMap<>(parameters);
    }
}
