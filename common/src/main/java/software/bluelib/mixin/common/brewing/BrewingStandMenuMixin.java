/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.common.brewing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.recipe.brewing.BrewingRecipe;
import software.bluelib.recipe.brewing.RecipeAwareSlot;

@Mixin(BrewingStandMenu.class)
public abstract class BrewingStandMenuMixin {

	@Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;)V", at = @At("TAIL"))
	private void captureLevel(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Container pBrewingStandContainer, @NotNull ContainerData pBrewingStandData, @NotNull CallbackInfo pCi) {
		var recipeManager = pPlayerInventory.player.level().getRecipeManager();
		for (Slot slot : ((BrewingStandMenu) (Object) this).slots) {
			if (slot instanceof RecipeAwareSlot awareSlot) {
				awareSlot.blueLib$setRecipeManager(recipeManager);
			}
		}
	}

	@WrapOperation(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/BrewingStandMenu$PotionSlot;mayPlaceItem(Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean blueLib$isPotionBottle(@NotNull ItemStack pStack, @NotNull Operation<Boolean> pOriginal, @Local(argsOnly = true) @NotNull Player pPlayer) {
		return BrewingRecipe.isBottle(pStack, pPlayer.level().getRecipeManager()) || pOriginal.call(pStack);
	}
}
