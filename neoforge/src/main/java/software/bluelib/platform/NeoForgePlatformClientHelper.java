package software.bluelib.platform;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.client.GeoRenderProvider;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoArmorRenderer;
import software.bluelib.loader.renderer.GeoRenderer;

public class NeoForgePlatformClientHelper implements IPlatformClient {


	@NotNull
	@Override
	public <T extends LivingEntity & GeoAnimatable> HumanoidModel<?> getArmorModelForItem(T pAnimatable, ItemStack pStack, EquipmentSlot pSlot, HumanoidModel<LivingEntity> pDefaultModel) {
		Item item = pStack.getItem();
		HumanoidModel<?> model = IClientItemExtensions.of(item).getHumanoidArmorModel(pAnimatable, pStack, pSlot, pDefaultModel);

		if (model == pDefaultModel && GeoRenderProvider.of(item).getGeoArmorRenderer(pAnimatable, pStack, pSlot, pDefaultModel) instanceof GeoArmorRenderer<?> geoArmorRenderer)
			return geoArmorRenderer;

		return model;
	}


	@Nullable
	@Override
	public GeoModel<?> getGeoModelForItem(ItemStack pItem) {
		if (IClientItemExtensions.of(pItem).getCustomRenderer() instanceof GeoRenderer<?> geoRenderer)
			return geoRenderer.getGeoModel();

		if (GeoRenderProvider.of(pItem).getGeoItemRenderer() instanceof GeoRenderer<?> geoRenderer)
			return geoRenderer.getGeoModel();

		return null;
	}


	@Nullable
	@Override
	public GeoModel<?> getGeoModelForArmor(ItemStack pArmour) {
		if (IClientItemExtensions.of(pArmour).getHumanoidArmorModel(null, pArmour, null, null) instanceof GeoArmorRenderer<?> armorRenderer)
			return armorRenderer.getGeoModel();

		if (GeoRenderProvider.of(pArmour).getGeoArmorRenderer(null, pArmour, null, null) instanceof GeoArmorRenderer<?> armorRenderer)
			return armorRenderer.getGeoModel();

		return null;
	}
}
