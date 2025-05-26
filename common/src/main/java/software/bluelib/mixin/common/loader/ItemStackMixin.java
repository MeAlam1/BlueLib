package software.bluelib.mixin.common.loader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.loader.GeckoLibConstants;
import software.bluelib.loader.util.InternalUtil;


@Mixin(ItemStack.class)
public class ItemStackMixin {
    
    @WrapOperation(method = "split", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack geckolib$removeGeckolibIdOnCopy(ItemStack instance, int count, Operation<ItemStack> original) {
        ItemStack copy = original.call(instance, count);

        if (count < instance.getCount() && copy.has(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get()))
            copy.remove(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get());

        return copy;
    }

    
    @WrapOperation(method = "isSameItemSameComponents", at = @At(value = "INVOKE", target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z"))
    private static boolean geckolib$skipGeckolibIdOnCompare(Object a, Object b, Operation<Boolean> original) {
        if (original.call(a, b))
            return true;

        if (!(a instanceof PatchedDataComponentMap components) || !(b instanceof PatchedDataComponentMap components2))
            return false;

        return InternalUtil.areComponentsMatchingIgnoringGeckoLibId(components, components2);
    }
}
