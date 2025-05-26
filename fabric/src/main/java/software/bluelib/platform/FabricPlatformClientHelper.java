package software.bluelib.platform;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.client.GeoRenderProvider;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoArmorRenderer;
import software.bluelib.loader.renderer.GeoRenderer;

public class FabricPlatformClientHelper implements IPlatformClient {

	@NotNull
	@Override
	public <T extends LivingEntity & GeoAnimatable> HumanoidModel<?> getArmorModelForItem(T pAnimatable, ItemStack pStack, EquipmentSlot pSlot, HumanoidModel<LivingEntity> pDefaultModel) {
		return GeoRenderProvider.of(pStack).getGeoArmorRenderer(pAnimatable, pStack, pSlot, pDefaultModel) instanceof GeoArmorRenderer<?> geoArmorRenderer ? geoArmorRenderer : pDefaultModel;
	}


	@Nullable
	@Override
	public GeoModel<?> getGeoModelForItem(ItemStack pItem) {
		if (GeoRenderProvider.of(pItem).getGeoItemRenderer() instanceof GeoRenderer<?> geoItemRenderer)
			return geoItemRenderer.getGeoModel();

		return null;
	}


	@Nullable
	@Override
	public GeoModel<?> getGeoModelForArmor(ItemStack pArmour) {
		if (GeoRenderProvider.of(pArmour).getGeoArmorRenderer(null, pArmour, null, null) instanceof GeoArmorRenderer<?> armorRenderer)
			return armorRenderer.getGeoModel();

		return null;
	}
}
