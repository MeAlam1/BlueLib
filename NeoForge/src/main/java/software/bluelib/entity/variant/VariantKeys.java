package software.bluelib.entity.variant;

public class VariantKeys extends VariantKeysBase {

    public VariantKeys(
            String pVariantName,
            String pEntityName
    ) {
        addParameter("VariantName", pVariantName);
        addParameter("EntityName", pEntityName);
    }

    public String getVariantName() {
        return (String) getParameter("VariantName");
    }

    public String getEntityName() {
        return (String) getParameter("EntityName");
    }
}
