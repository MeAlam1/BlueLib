/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DeferredBlueRenderProvider extends BlueRenderProvider {

	@NotNull
	MutableObject<BlueRenderProvider> getRenderProvider();

	@Override
	@Nullable
	default BlockEntityWithoutLevelRenderer getBlueItemRenderer() {
		return getRenderProvider().getValue().getBlueItemRenderer();
	}

	@Override
	@Nullable
	default <T extends LivingEntity> HumanoidModel<?> getBlueArmorRenderer(@Nullable T pLivingEntity, @NotNull ItemStack pItemStack, @Nullable EquipmentSlot pEquipmentSlot, @Nullable HumanoidModel<T> pOriginal) {
		return getRenderProvider().getValue().getBlueArmorRenderer(pLivingEntity, pItemStack, pEquipmentSlot, pOriginal);
	}
}
