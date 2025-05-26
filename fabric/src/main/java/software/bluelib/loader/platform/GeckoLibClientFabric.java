package software.bluelib.loader.platform;

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
import software.bluelib.loader.service.GeckoLibClient;


public class GeckoLibClientFabric implements GeckoLibClient {
    
    @NotNull
    @Override
    public <T extends LivingEntity & GeoAnimatable> HumanoidModel<?> getArmorModelForItem(T animatable, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> defaultModel) {
        return GeoRenderProvider.of(stack).getGeoArmorRenderer(animatable, stack, slot, defaultModel) instanceof GeoArmorRenderer<?> geoArmorRenderer ? geoArmorRenderer : defaultModel;
    }

    
    @Nullable
    @Override
    public GeoModel<?> getGeoModelForItem(ItemStack item) {
        if (GeoRenderProvider.of(item).getGeoItemRenderer() instanceof GeoRenderer<?> geoItemRenderer)
            return geoItemRenderer.getGeoModel();

        return null;
    }

    
    @Nullable
    @Override
    public GeoModel<?> getGeoModelForArmor(ItemStack armour) {
        if (GeoRenderProvider.of(armour).getGeoArmorRenderer(null, armour, null, null) instanceof GeoArmorRenderer<?> armorRenderer)
            return armorRenderer.getGeoModel();

        return null;
    }
}
