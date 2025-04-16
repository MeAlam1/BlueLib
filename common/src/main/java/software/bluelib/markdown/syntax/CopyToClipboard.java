// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@SuppressWarnings("unused")
public class CopyToClipboard {

    public static Boolean isCopyToClipboardEnabled = true;

    public MutableComponent apply(MutableComponent pMessage, String pTextToCopy) {
        if (!isCopyToClipboardEnabled) {
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
        return isCopyToClipboardEnabled;
    }
}
