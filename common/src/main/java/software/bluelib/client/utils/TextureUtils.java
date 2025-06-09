package software.bluelib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class TextureUtils {

    public static TextureManager getTextureManager() {
        return Minecraft.getInstance().getTextureManager();
    }

    public static AbstractTexture getTexture(ResourceLocation pTexturePath) {
        return getTextureManager().getTexture(pTexturePath);
    }
}
