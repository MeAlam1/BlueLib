// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.client.gui.logging;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LoggerScreen extends Screen {

    public LoggerScreen() {
        super(Component.translatable("bluelib.ui.logger.title"));
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int boxWidth = (int) (this.width * 0.9);
        int boxHeight = (int) (this.height * 0.9);
        int boxX = (this.width - boxWidth) / 2;
        int boxY = (this.height - boxHeight) / 2;

        pGuiGraphics.blit(MENU_BACKGROUND, boxX, boxY, 0, 0, boxWidth, boxHeight, 256, 256);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
