package software.bluelib.example.proxy;

import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLibConstants;

@Mod.EventBusSubscriber(modid = BlueLibConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    public void postInit() {
    }

    public void clientInit() {
    }
}
