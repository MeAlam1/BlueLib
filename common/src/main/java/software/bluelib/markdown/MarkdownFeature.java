package software.bluelib.markdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public abstract class MarkdownFeature {

    protected String prefix;
    protected String suffix;

    public MutableComponent apply(MutableComponent pComponent) {
        if (!isFeatureEnabled()) {
            BaseLogger.log(BaseLogLevel.INFO, getFeatureName() + " formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "(.*?)" + Pattern.quote(suffix));
        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentText(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblings(pComponent, pattern);
        }

        return result;
    }

    protected void processComponentText(String text, Style originalStyle, MutableComponent result, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.group(1).isEmpty()) {
                appendUnstyledText(text.substring(lastIndex, matcher.end()), result, originalStyle);
            } else if (matcher.start() > 0 && text.charAt(matcher.start() - 1) == '\\') {
                appendUnstyledText(text.substring(lastIndex, matcher.start() - 1), result, originalStyle);
                appendUnstyledText(matcher.group(0), result, originalStyle);
            } else {
                appendUnstyledText(text.substring(lastIndex, matcher.start()), result, originalStyle);
                appendFormattedText(matcher.group(1), originalStyle, result);
            }
            lastIndex = matcher.end();
        }

        appendUnstyledText(text.substring(lastIndex), result, originalStyle);
    }

    protected MutableComponent processSiblings(MutableComponent component, Pattern pattern) {
        MutableComponent result = Component.empty();

        for (Component sibling : component.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                processComponentText(mutableSibling.getString(), mutableSibling.getStyle(), result, pattern);
            } else {
                result.append(sibling);
            }
        }

        return result;
    }

    protected abstract void appendFormattedText(String text, Style originalStyle, MutableComponent result);

    protected void appendUnstyledText(String text, MutableComponent result, Style originalStyle) {
        result.append(Component.literal(text).setStyle(originalStyle));
    }

    protected abstract boolean isFeatureEnabled();

    protected abstract String getFeatureName();
}
