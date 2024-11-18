package software.bluelib.utils.markdown;

public abstract class MarkdownFeature {
    protected boolean enabled = true;
    protected String prefix;
    protected String suffix;

    public String apply(String pMessage) {
        if (!enabled) return pMessage;
        return pMessage.replaceAll(escapeRegex(prefix) + "(.*?)" + escapeRegex(suffix), applyFormat("$1"));
    }

    protected abstract String applyFormat(String pContent);

    public void setPrefixSuffix(String pNewPrefix, String pNewSuffix) {
        prefix = pNewPrefix;
        suffix = pNewSuffix;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    static String escapeRegex(String pInput) {
        return pInput.replaceAll("([\\\\*+\\[\\](){}|.^$?])", "\\\\$1");
    }
}
