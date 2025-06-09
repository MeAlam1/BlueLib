package software.bluelib.client.utils;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class ResourceUtils {

    public static ResourceManager getResourceManager() {
        return Minecraft.getInstance().getResourceManager();
    }

    public static Optional<Resource> getResource(ResourceLocation pResourcePath) {
        return getResourceManager().getResource(pResourcePath);
    }
}
