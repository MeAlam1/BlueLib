/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.common.loader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.DataUtils;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@WrapOperation(method = "split", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;"))
	public @NotNull ItemStack BlueLib$removeBlueLibIdOnCopy(@NotNull ItemStack pInstance, int pCount, @NotNull Operation<ItemStack> pOriginal) {
		ItemStack copy = pOriginal.call(pInstance, pCount);

		if (pCount < pInstance.getCount() && copy.has(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get()))
			copy.remove(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get());

		return copy;
	}

	@WrapOperation(method = "isSameItemSameComponents", at = @At(value = "INVOKE", target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z"))
	private static boolean BlueLib$skipBlueLibIdOnCompare(@NotNull Object pObjecta, @NotNull Object pObjectb, @NotNull Operation<Boolean> pOriginal) {
		if (pOriginal.call(pObjecta, pObjectb))
			return true;

		if (!(pObjecta instanceof PatchedDataComponentMap components) || !(pObjectb instanceof PatchedDataComponentMap components2))
			return false;

		return DataUtils.areComponentsMatchingIgnoringBlueId(components, components2);
	}
}
