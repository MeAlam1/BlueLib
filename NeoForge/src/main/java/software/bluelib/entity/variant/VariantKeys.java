package software.bluelib.entity.variant;

public class VariantKeys extends VariantKeysBase {

    public VariantKeys(
            String variantName,
            String entityName
    ) {
        AddParameter("VariantName", variantName);
        AddParameter("EntityName", entityName);
    }

    public String GetVariantName() {
        return (String) GetParameter("VariantName");
    }

    public String GetEntityType() {
        return (String) GetParameter("EntityName");
    }
}
