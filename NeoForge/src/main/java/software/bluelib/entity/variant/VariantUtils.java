package software.bluelib.entity.variant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VariantUtils {

    private static final Map<String, Map<String, String>> variantParametersMap = new HashMap<>();

    public static void processVariants(VariantRegistry pRegistry, Function<VariantKeys, Map<String, String>> pICustomParameter) {
        variantParametersMap.clear();
        List<VariantKeys> variants = pRegistry.getVariants();
        for (VariantKeys variant : variants) {
            Map<String, String> parameters = pICustomParameter.apply(variant);
            variantParametersMap.put(variant.getVariantName(), parameters);
        }
    }

    public static String getParameter(String pVariantName, String pParameterKey) {
        return variantParametersMap.getOrDefault(pVariantName, new HashMap<>()).getOrDefault(pParameterKey, "unknown");
    }
}