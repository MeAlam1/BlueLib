package software.bluelib.mixin.common.loader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.loader.GeckoLibConstants;


@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
    public ItemStack geckolib$removeGeckolibIdOnCopy(ItemStack instance, int count, Operation<ItemStack> original) {
        ItemStack copy = original.call(instance, count);

        if (copy.has(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get()))
            copy.remove(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get());

        return copy;
    }

    
    @WrapOperation(method = "synchronizeSlotToRemote", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean geckolib$forceGeckolibIdSync(ItemStack stack, ItemStack other, Operation<Boolean> original) {
        return original.call(stack, other) && stack.getOrDefault(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1).equals(other.getOrDefault(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1));
    }

    
    @WrapOperation(method = "triggerSlotListeners", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean geckolib$forceGeckolibSlotChange(ItemStack stack, ItemStack other, Operation<Boolean> original) {
        return original.call(stack, other) && stack.getOrDefault(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1).equals(other.getOrDefault(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), -1));
    }
}
