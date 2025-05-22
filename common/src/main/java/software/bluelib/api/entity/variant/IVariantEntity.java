/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.entity.variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.RandomSource;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.api.utils.variant.ParameterUtils;

@SuppressWarnings("unused")
public interface IVariantEntity {

    RandomSource random = RandomSource.create();

    default String getRandomVariant(List<String> pVariantNamesList, String pDefaultVariant) {
        if (pVariantNamesList.isEmpty()) {
            BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variant.list.empty", pDefaultVariant), true);
            return pDefaultVariant;
        }
        int index = random.nextInt(pVariantNamesList.size());
        String selectedVariant = pVariantNamesList.get(index);
        BaseLogger.log(BaseLogLevel.SUCCESS, BlueLibCommon.Translation.log("variant.random", selectedVariant, pVariantNamesList.size()), true);
        return selectedVariant;
    }

    default List<String> getEntityVariants(String pEntityName) {
        return new ArrayList<>(Objects.requireNonNull(ParameterUtils.getVariantsOfEntity(pEntityName)));
    }
}
