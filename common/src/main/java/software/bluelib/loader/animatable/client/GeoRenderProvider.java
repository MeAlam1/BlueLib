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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoItem;

public interface GeoRenderProvider {

    GeoRenderProvider DEFAULT = new GeoRenderProvider() {};

    static GeoRenderProvider of(ItemStack itemStack) {
        return of(itemStack.getItem());
    }

    static GeoRenderProvider of(Item item) {
        if (item instanceof GeoItem geoItem)
            return (GeoRenderProvider) geoItem.getRenderProvider();

        return DEFAULT;
    }

    @Nullable
    default BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
        return null;
    }

    @Nullable
    default <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
        return null;
    }
}
