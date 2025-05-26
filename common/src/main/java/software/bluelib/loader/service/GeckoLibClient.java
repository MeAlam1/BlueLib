package software.bluelib.loader.service;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.model.GeoModel;


public interface GeckoLibClient {
    
    @NotNull
    <T extends LivingEntity & GeoAnimatable> HumanoidModel<?> getArmorModelForItem(T animatable, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> defaultModel);

    
    @Nullable
    GeoModel<?> getGeoModelForItem(ItemStack item);

    
    @Nullable
    GeoModel<?> getGeoModelForArmor(ItemStack armour);
}
