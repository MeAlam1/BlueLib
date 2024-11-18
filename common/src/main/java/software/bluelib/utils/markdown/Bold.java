package software.bluelib.utils.markdown;

public class Bold extends MarkdownFeature {
    public Bold() {
        prefix = "**";
        suffix = "**";
    }

    @Override
    protected String applyFormat(String pContent) {
        return "§l" + pContent + "§r";
    }
}
