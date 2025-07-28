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
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animatable.client.BlueRenderProvider;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.BlueArmorRenderer;
import software.bluelib.loader.renderer.base.BlueRenderer;

public class FabricPlatformClientHelper implements IPlatformClient {

	@NotNull
	@Override
	public <T extends LivingEntity & BlueAnimatable> HumanoidModel<?> getArmorModelForItem(@NotNull T pAnimatable, @NotNull ItemStack pStack, @NotNull EquipmentSlot pSlot, @NotNull HumanoidModel<LivingEntity> pDefaultModel) {
		return BlueRenderProvider.of(pStack).getBlueArmorRenderer(pAnimatable, pStack, pSlot, pDefaultModel) instanceof BlueArmorRenderer<?> BlueArmorRenderer ? BlueArmorRenderer : pDefaultModel;
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
		if (BlueRenderProvider.of(pArmour).getBlueArmorRenderer(null, pArmour, null, null) instanceof BlueArmorRenderer<?> armorRenderer)
			return armorRenderer.getBlueModel();

		return null;
	}
}
