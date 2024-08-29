// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluetest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bluetest.event.ClientEvents;
import software.bluetest.event.ReloadHandler;
import software.bluetest.init.ModEntities;
import software.bluetest.proxy.ClientProxy;
import software.bluetest.proxy.CommonProxy;

@Mod(BlueTest.MODID)
public class BlueTest
{
    public static final String MODID = "bluetest";
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    //private static final Logger LOGGER = LogUtils.getLogger();

    public BlueTest() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(ReloadHandler.class);
        modEventBus.addListener(this::setupComplete);
    }

    private void setupComplete(final FMLLoadCompleteEvent event) {
        PROXY.postInit();
    }
}
