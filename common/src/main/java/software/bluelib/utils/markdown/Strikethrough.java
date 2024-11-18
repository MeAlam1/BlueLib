package software.bluelib.utils.markdown;

public class Strikethrough extends MarkdownFeature {
    public Strikethrough() {
        prefix = "~~";
        suffix = "~~";
    }

    @Override
    protected String applyFormat(String pContent) {
        return "§m" + pContent + "§r";
    }
}
