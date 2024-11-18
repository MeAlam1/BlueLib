package software.bluelib.utils.markdown;

public class Italic extends MarkdownFeature {
    public Italic() {
        prefix = "*";
        suffix = "*";
    }

    @Override
    protected String applyFormat(String pContent) {
        return "§o" + pContent + "§r";
    }
}
