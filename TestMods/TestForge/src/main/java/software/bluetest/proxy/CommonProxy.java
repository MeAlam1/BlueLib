package software.bluetest.proxy;

import net.minecraftforge.fml.common.Mod;
import software.bluetest.BlueTest;

@Mod.EventBusSubscriber(modid = BlueTest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    public void postInit() {}

    public void clientInit() {
    }
}
