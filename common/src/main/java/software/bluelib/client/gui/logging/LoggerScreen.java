// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.client.gui.logging;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.LogCache;

public class LoggerScreen extends Screen {

    private int scrollOffset = 0;
    private static final int LINE_HEIGHT = 10;

    public LoggerScreen() {
        super(BlueLibCommon.Translation.translate("ui.logger.title"));
    }

    @Override
    protected void init() {
        super.init();
        scrollOffset = 0;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int boxWidth = (int) (this.width * 0.9);
        int boxHeight = (int) (this.height * 0.9);
        int boxX = (this.width - boxWidth) / 2;
        int boxY = (this.height - boxHeight) / 2;

        pGuiGraphics.fill(boxX - 2, boxY - 22, boxX + boxWidth + 2, boxY + boxHeight + 2, 0xDD000000);

        pGuiGraphics.fill(boxX, boxY - 20, boxX + boxWidth, boxY, 0xFF444444);
        pGuiGraphics.drawCenteredString(this.font, this.title, boxX + boxWidth / 2, boxY - 15, 0xFFFFFF);

        List<LogCache.LogEntry> logEntries = LogCache.getLogs();
        List<RenderedLine> renderedLines = new ArrayList<>();

        for (LogCache.LogEntry entry : logEntries) {
            List<String> wrapped = wrapText(entry.message(), boxWidth - 15);
            for (String line : wrapped) {
                renderedLines.add(new RenderedLine(line, entry.color()));
            }
        }

        int maxVisibleLines = (boxHeight - 10) / LINE_HEIGHT;
        int maxOffset = Math.max(0, renderedLines.size() - maxVisibleLines);
        scrollOffset = Math.min(scrollOffset, maxOffset);

        int y = boxY + 5;
        for (int i = scrollOffset; i < renderedLines.size() && y < boxY + boxHeight - LINE_HEIGHT; i++) {
            RenderedLine line = renderedLines.get(i);
            pGuiGraphics.drawString(this.font, line.text(), boxX + 7, y, line.color());
            y += LINE_HEIGHT;
        }

        // Scrollbar
        if (renderedLines.size() > maxVisibleLines) {
            int scrollbarHeight = (int) ((float) maxVisibleLines / renderedLines.size() * (boxHeight - 10));
            int scrollbarY = boxY + 5 + (int) ((float) scrollOffset / renderedLines.size() * (boxHeight - 10));
            pGuiGraphics.fill(boxX + boxWidth - 6, scrollbarY, boxX + boxWidth - 3, scrollbarY + scrollbarHeight, 0xFF888888);
        }
    }

    private record RenderedLine(String text, int color) {}

    private List<String> wrapText(String pText, int pMaxWidth) {
        List<String> lines = new ArrayList<>();
        String[] segments = pText.split("\n");

        for (String segment : segments) {
            String[] words = segment.split(" ");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                int wordWidth = this.font.width(word);
                if (wordWidth > pMaxWidth) {
                    while (!word.isEmpty()) {
                        int splitIndex = getSplitIndex(word, pMaxWidth);
                        lines.add(word.substring(0, splitIndex));
                        word = word.substring(splitIndex);
                    }
                    continue;
                }

                int lineWidth = this.font.width(currentLine + word + " ");
                if (lineWidth > pMaxWidth) {
                    lines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder(word + " ");
                } else {
                    currentLine.append(word).append(" ");
                }
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString().trim());
            }
        }

        return lines;
    }

    private int getSplitIndex(String pWord, int pMaxWidth) {
        for (int i = 1; i <= pWord.length(); i++) {
            if (this.font.width(pWord.substring(0, i)) > pMaxWidth) {
                return i - 1;
            }
        }
        return pWord.length();
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        if (pScrollY > 0) {
            scrollOffset = Math.max(0, scrollOffset - 1);
        } else if (pScrollY < 0) {
            scrollOffset++;
        }
        return true;
    }
}
