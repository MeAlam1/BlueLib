/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bluelib.oldLoader.animatable.BlueItem;

public interface BlueRenderProvider {

	BlueRenderProvider DEFAULT = new BlueRenderProvider() {};

	static BlueRenderProvider of(ItemStack pItemStack) {
		return of(pItemStack.getItem());
	}

	static BlueRenderProvider of(Item pItem) {
		if (pItem instanceof BlueItem BlueItem)
			return (BlueRenderProvider) BlueItem.getRenderProvider();

		return DEFAULT;
	}

	@Nullable
	default BlockEntityWithoutLevelRenderer getBlueItemRenderer() {
		return null;
	}

	@Nullable
	default <T extends LivingEntity> HumanoidModel<?> getBlueArmorRenderer(@Nullable T pLivingEntity, ItemStack pItemStack, @Nullable EquipmentSlot pEquipmentSlot, @Nullable HumanoidModel<T> pOriginal) {
		return null;
	}
}
