package software.bluelib.platform;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.model.GeoModel;

public interface IPlatformClient {

	@NotNull
	<T extends LivingEntity & GeoAnimatable> HumanoidModel<?> getArmorModelForItem(T pAnimatable, ItemStack pStack, EquipmentSlot pSlot, HumanoidModel<LivingEntity> pDefaultModel);

	@Nullable
	GeoModel<?> getGeoModelForItem(ItemStack pItem);

	@Nullable
	GeoModel<?> getGeoModelForArmor(ItemStack pArmour);
}
