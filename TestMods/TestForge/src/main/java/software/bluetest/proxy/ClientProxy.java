package software.bluetest.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import software.bluetest.BlueTest;
import software.bluetest.event.ClientEvents;

@Mod.EventBusSubscriber(modid = BlueTest.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void postInit() {}

    @Override
    public void clientInit() {
        super.clientInit();
        ClientEvents.registerRenderers();
    }
}
