// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.entity.variant;

import java.util.List;
import net.minecraft.util.RandomSource;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.base.IVariantEntityBase;

@SuppressWarnings("unused")
public interface IVariantEntity extends IVariantEntityBase {

    RandomSource random = RandomSource.create();

    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        if (pVariantNamesList.isEmpty()) {
            BaseLogger.log(BaseLogLevel.INFO, "Variant names list is empty. Returning default variant: " + pDefaultVariant, true);
            return pDefaultVariant;
        }
        int index = random.nextInt(pVariantNamesList.size());
        String selectedVariant = pVariantNamesList.get(index);
        BaseLogger.log(BaseLogLevel.SUCCESS, "Selected random variant: " + selectedVariant + " from list of size: " + pVariantNamesList.size(), true);
        return selectedVariant;
    }
}
