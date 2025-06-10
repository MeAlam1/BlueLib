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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.BlueLibConstants;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
    public ItemStack BlueLib$removeBlueLibIdOnCopy(ItemStack instance, int count, Operation<ItemStack> original) {
        ItemStack copy = original.call(instance, count);

        if (copy.has(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get()))
            copy.remove(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get());

        return copy;
    }

    @WrapOperation(method = "synchronizeSlotToRemote", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean BlueLib$forceBlueLibIdSync(ItemStack stack, ItemStack other, Operation<Boolean> original) {
        return original.call(stack, other) && stack.getOrDefault(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1).equals(other.getOrDefault(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1));
    }

    @WrapOperation(method = "triggerSlotListeners", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean BlueLib$forceBlueLibSlotChange(ItemStack stack, ItemStack other, Operation<Boolean> original) {
        return original.call(stack, other) && stack.getOrDefault(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1).equals(other.getOrDefault(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1));
    }
}
