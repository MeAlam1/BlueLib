package software.bluelib.example.proxy;

import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLib;

@Mod.EventBusSubscriber(modid = BlueLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    public void postInit() {}

    public void clientInit() {
    }
}
