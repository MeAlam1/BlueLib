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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.armor.BlueArmorRenderer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.oldLoader.animatable.client.BlueRenderProvider;

public class NeoForgePlatformClientHelper implements IPlatformClient {

	@NotNull
	@Override
	public <T extends LivingEntity & BlueAnimatable> HumanoidModel<?> getArmorModelForItem(@NotNull T pAnimatable, @NotNull ItemStack pStack, @NotNull EquipmentSlot pSlot, @NotNull HumanoidModel<LivingEntity> pDefaultModel) {
		Item item = pStack.getItem();
		HumanoidModel<?> model = IClientItemExtensions.of(item).getHumanoidArmorModel(pAnimatable, pStack, pSlot, pDefaultModel);

		if (model == pDefaultModel && BlueRenderProvider.of(item).getBlueArmorRenderer(pAnimatable, pStack, pSlot, pDefaultModel) instanceof BlueArmorRenderer<?> BlueArmorRenderer)
			return BlueArmorRenderer;

		return model;
	}

	@Nullable
	@Override
	public BlueModel<?> getBlueModelForItem(@NotNull ItemStack pItem) {
		if (IClientItemExtensions.of(pItem).getCustomRenderer() instanceof BlueRenderer<?> blueRenderer)
			return blueRenderer.getBlueModel();

		if (BlueRenderProvider.of(pItem).getBlueItemRenderer() instanceof BlueRenderer<?> blueRenderer)
			return blueRenderer.getBlueModel();

		return null;
	}

	@Nullable
	@Override
	public BlueModel<?> getBlueModelForArmor(@NotNull ItemStack pArmour) {
		if (IClientItemExtensions.of(pArmour).getHumanoidArmorModel(null, pArmour, null, null) instanceof BlueArmorRenderer<?> armorRenderer)
			return armorRenderer.getBlueModel();

		if (BlueRenderProvider.of(pArmour).getBlueArmorRenderer(null, pArmour, null, null) instanceof BlueArmorRenderer<?> armorRenderer)
			return armorRenderer.getBlueModel();

		return null;
	}
}
