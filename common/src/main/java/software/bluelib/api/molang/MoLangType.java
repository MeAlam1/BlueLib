package software.bluelib.api.molang;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public record MoLangType(String id, String name) {

    private static final Map<String, MoLangType> REGISTRY = new LinkedHashMap<>();

    public MoLangType(String id, String name) {
        this.id = id;
        this.name = name;
        if (REGISTRY.containsKey(id)) {
            throw new IllegalArgumentException("MoLangType with id '" + id + "' is already registered.");
        }
        REGISTRY.put(id, this);
    }

    public static MoLangType byId(String pId) {
        return REGISTRY.get(pId);
    }

    public static Collection<MoLangType> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    @Override
    public @NotNull String toString() {
        return name;
    }

    public static final MoLangType GENERAL = new MoLangType("g", "general");
    public static final MoLangType MATH = new MoLangType("m", "math");
    public static final MoLangType LIVING_ENTITY = new MoLangType("le", "living_entity");
    public static final MoLangType ANIMATABLE = new MoLangType("q", "animatable");
}
