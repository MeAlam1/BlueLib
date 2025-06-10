/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

public interface DeferredBlueRenderProvider extends BlueRenderProvider {

    MutableObject<BlueRenderProvider> getRenderProvider();

    @Override
    @Nullable
    default BlockEntityWithoutLevelRenderer getBlueItemRenderer() {
        return getRenderProvider().getValue().getBlueItemRenderer();
    }

    @Override
    @Nullable
    default <T extends LivingEntity> HumanoidModel<?> getBlueArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
        return getRenderProvider().getValue().getBlueArmorRenderer(livingEntity, itemStack, equipmentSlot, original);
    }
}
