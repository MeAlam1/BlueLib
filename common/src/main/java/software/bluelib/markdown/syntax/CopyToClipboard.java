// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.config.MarkdownConfig;

@SuppressWarnings("unused")
public class CopyToClipboard {

    public MutableComponent apply(MutableComponent pMessage, String pTextToCopy) {
        if (!MarkdownConfig.isCopyToClipboardEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("markdown.copyToClipboard.disabled"), true);
            return pMessage;
        }
        MutableComponent result = Component.empty();

        for (Component sibling : pMessage.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                if (mutableSibling.getStyle().getClickEvent() == null) {
                    mutableSibling.setStyle(mutableSibling.getStyle()
                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, pTextToCopy)));
                }
                result.append(mutableSibling);
            } else {
                result.append(sibling);
            }
        }

        return result;
    }

    public static Boolean isCopyToClipboardEnabled() {
        return MarkdownConfig.isCopyToClipboardEnabled;
    }
}
