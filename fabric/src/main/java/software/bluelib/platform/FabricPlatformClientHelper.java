/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.armor.BlueArmorRenderer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.client.BlueRenderProvider;

public class FabricPlatformClientHelper implements IPlatformClient {

	@Override
	@SuppressWarnings("unchecked")
	public <T extends LivingEntity & BlueAnimatable> @NotNull HumanoidModel<T> getArmorModelForItem(
			@NotNull T pAnimatable,
			@NotNull ItemStack pStack,
			@NotNull EquipmentSlot pSlot,
			@NotNull HumanoidModel<LivingEntity> pDefaultModel) {
		Object renderer = BlueRenderProvider.of(pStack).getBlueArmorRenderer(pAnimatable, pStack, pSlot, pDefaultModel);
		if (renderer instanceof HumanoidModel<?> model) {
			return (HumanoidModel<T>) model;
		}
		return (HumanoidModel<T>) (HumanoidModel) pDefaultModel;
	}

	@Nullable
	@Override
	public BlueModel<?> getBlueModelForItem(@NotNull ItemStack pItem) {
		if (BlueRenderProvider.of(pItem).getBlueItemRenderer() instanceof BlueRenderer<?> BlueItemRenderer)
			return BlueItemRenderer.getBlueModel();

		return null;
	}

	@Nullable
	@Override
	public BlueModel<?> getBlueModelForArmor(@NotNull ItemStack pArmour) {
		if (BlueRenderProvider.of(pArmour).getBlueArmorRenderer(null, pArmour, null, null) instanceof BlueArmorRenderer<?, ?> armorRenderer)
			return armorRenderer.getBlueModel();

		return null;
	}
}
