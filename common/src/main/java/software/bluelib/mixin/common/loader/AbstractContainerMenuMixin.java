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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.internal.registry.BlueDataComponentRegistry;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

	@WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
	public ItemStack BlueLib$removeBlueLibIdOnCopy(@NotNull ItemStack pInstance, int pCount, @NotNull Operation<ItemStack> pOriginal) {
		ItemStack copy = pOriginal.call(pInstance, pCount);

		if (copy.has(BlueDataComponentRegistry.STACK_ANIMATABLE_ID.get()))
			copy.remove(BlueDataComponentRegistry.STACK_ANIMATABLE_ID.get());

		return copy;
	}

	@WrapOperation(method = "synchronizeSlotToRemote", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	public boolean BlueLib$forceBlueLibIdSync(@NotNull ItemStack pStack, @NotNull ItemStack pOther, @NotNull Operation<Boolean> pOriginal) {
		return pOriginal.call(pStack, pOther) && pStack.getOrDefault(BlueDataComponentRegistry.STACK_ANIMATABLE_ID.get(), -1).equals(pOther.getOrDefault(BlueDataComponentRegistry.STACK_ANIMATABLE_ID.get(), -1));
	}

	@WrapOperation(method = "triggerSlotListeners", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	public boolean BlueLib$forceBlueLibSlotChange(@NotNull ItemStack pStack, @NotNull ItemStack pOther, @NotNull Operation<Boolean> pOriginal) {
		return pOriginal.call(pStack, pOther) && pStack.getOrDefault(BlueDataComponentRegistry.STACK_ANIMATABLE_ID.get(), -1).equals(pOther.getOrDefault(BlueDataComponentRegistry.STACK_ANIMATABLE_ID.get(), -1));
	}
}
