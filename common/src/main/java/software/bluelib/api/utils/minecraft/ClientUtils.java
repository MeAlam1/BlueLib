package software.bluelib.api.utils.minecraft;

import net.minecraft.client.Minecraft;

public class ClientUtils {
    public static boolean isInWorld() {
        return Minecraft.getInstance().level != null;
    }
}
