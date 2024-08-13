package software.bluelib.entity.variant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import software.bluelib.BlueLib;

import java.util.List;

public interface IVariantEntity {
    RandomSource random = RandomSource.create();

    default ResourceLocation GetTextureLocation() {
        return new ResourceLocation(BlueLib.MODID, "textures/entity/" + this.GetEntityName() + "/" + this.GetVariantName() + ".png");
    }

    String GetVariantName();

    String GetEntityName();

    ResourceLocation GetVariantResource();

    default String ReturnRandomVariant(String pDefaultVariant) {
        List<String> entityVariantNames = List.of(GetEntityName());
        List<String> spawnableVariants = entityVariantNames.stream()
                .toList();
        if (!spawnableVariants.isEmpty()) {
            return spawnableVariants.get(this.random.nextInt(spawnableVariants.size()));
        }
        return pDefaultVariant;
    }
}
