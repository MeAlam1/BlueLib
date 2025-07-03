/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils;

import java.util.Objects;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;

@SuppressWarnings("unused")
public class DataUtils {

	public static boolean areComponentsMatchingIgnoringBlueId(@NotNull PatchedDataComponentMap pComponentMap, @NotNull PatchedDataComponentMap pComponentMapTwo) {
		final DataComponentType<Long> stackId = BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get();
		boolean patched = false;

		if (pComponentMap.has(stackId)) {
			PatchedDataComponentMap prevMap = pComponentMap;
			boolean copyOnWrite = prevMap.copyOnWrite;
			(pComponentMap = pComponentMap.copy()).remove(stackId);
			pComponentMap.copyOnWrite = copyOnWrite;
			patched = true;
		}

		if (pComponentMapTwo.has(stackId)) {
			PatchedDataComponentMap prevMap = pComponentMapTwo;
			boolean copyOnWrite = prevMap.copyOnWrite;
			(pComponentMapTwo = pComponentMapTwo.copy()).remove(stackId);
			pComponentMapTwo.copyOnWrite = copyOnWrite;
			patched = true;
		}

		return patched && Objects.equals(pComponentMap, pComponentMapTwo);
	}
}
