package software.bluelib.utils.markdown;

public class Underline extends MarkdownFeature {
    public Underline() {
        prefix = "__";
        suffix = "__";
    }

    @Override
    protected String applyFormat(String pContent) {
        return "§n" + pContent + "§r";
    }
}
