package software.bluelib.platform;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animatable.client.BlueRenderProvider;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.BlueArmorRenderer;
import software.bluelib.loader.renderer.BlueRenderer;

public class FabricPlatformClientHelper implements IPlatformClient {

	@NotNull
	@Override
	public <T extends LivingEntity & BlueAnimatable> HumanoidModel<?> getArmorModelForItem(T pAnimatable, ItemStack pStack, EquipmentSlot pSlot, HumanoidModel<LivingEntity> pDefaultModel) {
		return BlueRenderProvider.of(pStack).getBlueArmorRenderer(pAnimatable, pStack, pSlot, pDefaultModel) instanceof BlueArmorRenderer<?> BlueArmorRenderer ? BlueArmorRenderer : pDefaultModel;
	}


	@Nullable
	@Override
	public BlueModel<?> getBlueModelForItem(ItemStack pItem) {
		if (BlueRenderProvider.of(pItem).getBlueItemRenderer() instanceof BlueRenderer<?> BlueItemRenderer)
			return BlueItemRenderer.getBlueModel();

		return null;
	}


	@Nullable
	@Override
	public BlueModel<?> getBlueModelForArmor(ItemStack pArmour) {
		if (BlueRenderProvider.of(pArmour).getBlueArmorRenderer(null, pArmour, null, null) instanceof BlueArmorRenderer<?> armorRenderer)
			return armorRenderer.getBlueModel();

		return null;
	}
}
