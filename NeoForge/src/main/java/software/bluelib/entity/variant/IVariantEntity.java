package software.bluelib.entity.variant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.stream.Collectors;

public interface IVariantEntity {
    RandomSource random = RandomSource.create();

    default ResourceLocation getTextureLocation(String pModId, String pPath) {
        return new ResourceLocation(pModId, pPath);
    }

    String getVariantName();

    String getEntityName();

    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        List<String> spawnableVariants = pVariantNamesList.stream()
                .toList();
        if (!spawnableVariants.isEmpty()) {
            return spawnableVariants.get(this.random.nextInt(spawnableVariants.size()));
        }
        return pDefaultVariant;
    }

    default List<String> getEntityVariants(String pEntityName, VariantRegistry pVariantRegistry) {
        return pVariantRegistry.getVariants().stream()
                .filter(variant -> pEntityName.equals(variant.getEntityName()))
                .map(VariantKeys::getVariantName)
                .collect(Collectors.toList());
    }
}
