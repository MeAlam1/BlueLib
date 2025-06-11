package software.bluelib.api.registry.builders;

import software.bluelib.api.registry.AbstractRegistryBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuilderUtils {
    public static final String modId = AbstractRegistryBuilder.getModID();
    private BuilderUtils() {}

    public static String getBaseId(String fullId, String[] suffixes) {
        for (String suffix : suffixes) {
            if (fullId.endsWith(suffix)) {
                return fullId.substring(0, fullId.length() - suffix.length());
            }
        }
        return fullId;
    }

    public static <T> boolean addIfAbsent(Set<T> set, T value) {
        return set.add(value);
    }

    public static <T> void addAllIfAbsent(Set<T> set, List<T> values) {
        for (T value : values) {
            set.add(value);
        }
    }

    public static <T> Set<T> newDedupSet() {
        return new HashSet<>();
    }
}
