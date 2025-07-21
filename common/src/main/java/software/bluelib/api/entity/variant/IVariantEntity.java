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
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.api.utils.variant.ParameterUtils;
import software.bluelib.entity.variant.IVariantAccessor;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public interface IVariantEntity<T extends Entity> {

	@NotNull
	RandomSource random = RandomSource.create();

	@NotNull
	T getEntity();

	@Nullable
	default String getRandomVariant(@NotNull List<String> pVariantNamesList, @Nullable String pDefaultVariant) {
		if (pVariantNamesList.isEmpty()) {
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("variant.list.empty", pDefaultVariant));
			return pDefaultVariant;
		}
		int index = random.nextInt(pVariantNamesList.size());
		String selectedVariant = pVariantNamesList.get(index);
		BaseLogger.log(true, BaseLogLevel.SUCCESS, BlueTranslation.log("variant.random", selectedVariant, pVariantNamesList.size()));
		return selectedVariant;
	}

	@Nullable
	default List<String> getEntityVariants(@NotNull ResourceLocation pEntity) {
		Set<String> variants = ParameterUtils.getVariantsOfEntity(pEntity);
		return variants != null ? new ArrayList<>(variants) : null;
	}

	@NotNull
	default String getVariantName() {
		T entity = getEntity();
		return ((IVariantAccessor) entity).getEntityVariantName();
	}

	default void setVariantName(@NotNull String pVariantName) {
		T entity = getEntity();
		((IVariantAccessor) entity).setEntityVariantName(pVariantName);
	}
}
