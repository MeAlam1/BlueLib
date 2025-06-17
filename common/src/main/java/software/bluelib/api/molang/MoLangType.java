package software.bluelib.api.molang;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public record MoLangType(String pId) {

    private static final Map<String, MoLangType> REGISTRY = new LinkedHashMap<>();

    public MoLangType(String pId) {
        this.pId = pId;
        if (REGISTRY.containsKey(pId)) {
            throw new IllegalArgumentException("MoLangType with pId '" + pId + "' is already registered.");
        }
        REGISTRY.put(pId, this);
    }

    public static MoLangType byId(String pId) {
        return REGISTRY.get(pId);
    }

    public static Collection<MoLangType> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    @Override
    public String toString() {
        return pId;
    }

    public static final MoLangType GENERAL = new MoLangType("general");
}
