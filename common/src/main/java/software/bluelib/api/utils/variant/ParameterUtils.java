package software.bluelib.api.utils.variant;

import com.google.gson.JsonElement;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.variants.EntityCache;
import software.bluelib.loader.cache.variants.VariantCache;

public class ParameterUtils {

    private ParameterUtils() {}

    @NotNull
    public static Set<ResourceLocation> getAllEntities() {
        return ResourceCache.getVariants().keySet();
    }

    @Nullable
    public static Set<String> getVariantsOfEntity(ResourceLocation pEntity) {
        EntityCache entityCache = ResourceCache.getVariants().get(pEntity);
        if (entityCache == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, "Entity not found: " + pEntity);
            return null;
        }
        return entityCache.getVariantNames();
    }

    @Nullable
    public static JsonElement getParameterDataForVariant(ResourceLocation pEntity, String pVariantName, String pParameter, @NotNull String pFallbackVariant) {
        JsonElement result = getParameterDataForVariant(pEntity, pVariantName, pParameter);
        if (result == null && !pFallbackVariant.equals(pVariantName)) {
            result = getParameterDataForVariant(pEntity, pFallbackVariant, pParameter);
        }
        return result;
    }

    @Nullable
    public static JsonElement getParameterDataForVariant(ResourceLocation pEntity, String pVariantName, String pParameter) {
        EntityCache entityCache = ResourceCache.getVariants().get(pEntity);
        if (entityCache == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, "Entity not found: " + pEntity);
            return null;
        }
        VariantCache variantCache = entityCache.getVariant(pVariantName);
        if (variantCache == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, "Variant not found: " + pVariantName + " for entity: " + pEntity);
            return null;
        }
        JsonElement parameterElement = variantCache.getParameter(pParameter);
        if (parameterElement == null) {
            BaseLogger.log(true, BaseLogLevel.INFO, "Parameter not found: " + pParameter + " in variant: " + pVariantName + " for entity: " + pEntity);
        }
        return parameterElement;
    }
}
