package software.bluelib.utils.markdown;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class Hyperlink extends MarkdownFeature {
    public Hyperlink() {
        prefix = "[";
        suffix = "]";
    }

    @Override
    protected String applyFormat(String pContent) {
        String[] parts = pContent.split("\\]\\(");
        if (parts.length == 2) {
            String text = parts[0];
            String url = parts[1].substring(0, parts[1].length() - 1);

            Component linkComponent = Component.literal(text)
                    .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));

            BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink created: " + text + " -> " + url, true);
            return linkComponent.getString();
        }
        BaseLogger.log(BaseLogLevel.WARNING, "Failed to parse hyperlink: " + pContent, true);
        return pContent;
    }

    @Override
    public String apply(String pMessage) {
        if (!enabled) return pMessage;
        return pMessage.replaceAll(escapeRegex(prefix) + "(.*?)\\]" + escapeRegex(suffix) + "\\((.*?)\\)", "$1$2");
    }
}
