// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.variant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.entity.variant.VariantParameter;

import java.util.List;
import java.util.stream.Collectors;

public interface IVariantEntity {
    RandomSource random = RandomSource.create();

    default ResourceLocation getTextureLocation(String pModId, String pPath) {
        return new ResourceLocation(pModId, pPath);
    }

    String getVariantName();

    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        List<String> spawnableVariants = pVariantNamesList.stream()
                .toList();
        if (!spawnableVariants.isEmpty()) {
            return spawnableVariants.get(this.random.nextInt(spawnableVariants.size()));
        }
        return pDefaultVariant;
    }

    default List<String> getEntityVariants(String pEntityName, VariantLoader pVariantLoader) {
        return pVariantLoader.getVariants().stream()
                .filter(variant -> pEntityName.equals(variant.getEntityName()))
                .map(VariantParameter::getVariantName)
                .collect(Collectors.toList());
    }
}
