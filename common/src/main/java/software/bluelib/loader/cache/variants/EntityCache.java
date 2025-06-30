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
