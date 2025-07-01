/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
    public static Set<String> getVariantsOfEntity(@NotNull ResourceLocation pEntity) {
        EntityCache entityCache = getOptionalEntityCache(pEntity);
        if (entityCache == null) return null;
        return entityCache.getVariantNames();
    }

    @Nullable
    public static JsonElement getParameterDataForVariant(@NotNull ResourceLocation pEntity, @NotNull String pVariantName, @NotNull String pParameter, @NotNull String pFallbackVariant) {
        JsonElement result = getParameterDataForVariant(pEntity, pVariantName, pParameter);
        if (result == null && !pFallbackVariant.equals(pVariantName)) {
            result = getParameterDataForVariant(pEntity, pFallbackVariant, pParameter);
        }
        return result;
    }

    @Nullable
    public static JsonElement getParameterDataForVariant(@NotNull ResourceLocation pEntity, @NotNull String pVariantName, @NotNull String pParameter) {
        EntityCache entityCache = getOptionalEntityCache(pEntity);
        if (entityCache == null) return null;
        VariantCache variantCache = getOptionalVariantCache(pEntity, pVariantName, entityCache);
        if (variantCache == null) return null;
        return getOptionalParameter(pEntity, pVariantName, pParameter, variantCache);
    }

    public static @Nullable JsonElement getOptionalParameter(@NotNull ResourceLocation pEntity, @NotNull String pVariantName, @NotNull String pParameter, @NotNull VariantCache pVariantCache) {
        JsonElement parameterElement = pVariantCache.getParameter(pParameter);
        if (parameterElement == null) {
            BaseLogger.log(true, BaseLogLevel.INFO, "Parameter not found: " + pParameter + " in variant: " + pVariantName + " for entity: " + pEntity);
        }
        return parameterElement;
    }

    public static @Nullable VariantCache getOptionalVariantCache(@NotNull ResourceLocation pEntity, @NotNull String pVariantName, @NotNull EntityCache pEntityCache) {
        VariantCache variantCache = pEntityCache.getVariant(pVariantName);
        if (variantCache == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, "Variant not found: " + pVariantName + " for entity: " + pEntity);
            return null;
        }
        return variantCache;
    }

    public static @Nullable EntityCache getOptionalEntityCache(@NotNull ResourceLocation pEntity) {
        EntityCache entityCache = ResourceCache.getVariants().get(pEntity);
        if (entityCache == null) {
            BaseLogger.log(true, BaseLogLevel.WARNING, "Entity not found: " + pEntity);
            return null;
        }
        return entityCache;
    }
}
