// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.utils.variant.ParameterUtils;

@SuppressWarnings("unused")
public interface IVariantEntityBase {

    default ResourceLocation getTextureLocation(String pModId, String pPath) {
        return ResourceLocation.fromNamespaceAndPath(pModId, pPath);
    }

    default List<String> getEntityVariants(String pEntityName) {
        return new ArrayList<>(Objects.requireNonNull(ParameterUtils.getVariantsOfEntity(pEntityName)));
    }
}
