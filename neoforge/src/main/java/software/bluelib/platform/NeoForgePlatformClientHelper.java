package software.bluelib.platform;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animatable.client.BlueRenderProvider;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.BlueArmorRenderer;
import software.bluelib.loader.renderer.BlueRenderer;

public class NeoForgePlatformClientHelper implements IPlatformClient {


	@NotNull
	@Override
	public <T extends LivingEntity & BlueAnimatable> HumanoidModel<?> getArmorModelForItem(T pAnimatable, ItemStack pStack, EquipmentSlot pSlot, HumanoidModel<LivingEntity> pDefaultModel) {
		Item item = pStack.getItem();
		HumanoidModel<?> model = IClientItemExtensions.of(item).getHumanoidArmorModel(pAnimatable, pStack, pSlot, pDefaultModel);

		if (model == pDefaultModel && BlueRenderProvider.of(item).getBlueArmorRenderer(pAnimatable, pStack, pSlot, pDefaultModel) instanceof BlueArmorRenderer<?> BlueArmorRenderer)
			return BlueArmorRenderer;

		return model;
	}


	@Nullable
	@Override
	public BlueModel<?> getBlueModelForItem(ItemStack pItem) {
		if (IClientItemExtensions.of(pItem).getCustomRenderer() instanceof BlueRenderer<?> blueRenderer)
			return blueRenderer.getBlueModel();

		if (BlueRenderProvider.of(pItem).getBlueItemRenderer() instanceof BlueRenderer<?> blueRenderer)
			return blueRenderer.getBlueModel();

		return null;
	}


	@Nullable
	@Override
	public BlueModel<?> getBlueModelForArmor(ItemStack pArmour) {
		if (IClientItemExtensions.of(pArmour).getHumanoidArmorModel(null, pArmour, null, null) instanceof BlueArmorRenderer<?> armorRenderer)
			return armorRenderer.getBlueModel();

		if (BlueRenderProvider.of(pArmour).getBlueArmorRenderer(null, pArmour, null, null) instanceof BlueArmorRenderer<?> armorRenderer)
			return armorRenderer.getBlueModel();

		return null;
	}
}
