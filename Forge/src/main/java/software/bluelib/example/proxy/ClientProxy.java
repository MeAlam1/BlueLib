package software.bluelib.example.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.event.ClientEvents;

@Mod.EventBusSubscriber(modid = BlueLibConstants.MOD_ID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void postInit() {
    }

    @Override
    public void clientInit() {
        super.clientInit();
        ClientEvents.registerRenderers();
    }
}
