package software.bluetest.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.utils.ParameterUtils;
import software.bluetest.BlueTest;
import software.bluelib.entity.variant.VariantParameter;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BlueTest.MODID)
public class ReloadEventHandler {
    public static final VariantLoader texturesLoader = new VariantLoader();

    @SubscribeEvent
    public static void onDataPackReload(ServerStartingEvent pEvent) {
        System.out.println("Data packs are being reloaded!");
        loadEntityVariants(pEvent);
    }

    public static void loadEntityVariants(ServerStartingEvent pEvent) {
        final ResourceLocation JsonLocationMod = new ResourceLocation(BlueTest.MODID, "variant/entity/dragon.json");
        final ResourceLocation JsonLocationPack = new ResourceLocation(BlueTest.MODID, "variant/entity/dragondata.json");
        texturesLoader.loadVariants(JsonLocationMod, JsonLocationPack, pEvent.getServer());


        }
}

