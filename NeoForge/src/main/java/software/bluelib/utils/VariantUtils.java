package software.bluelib.utils;

import software.bluelib.entity.variant.VariantParameter;
import software.bluelib.entity.variant.VariantLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VariantUtils {

    private static final Map<String, Map<String, String>> variantParametersMap = new HashMap<>();

    public static void processVariants(VariantLoader pLoader, Function<VariantParameter, Map<String, String>> pICustomParameter) {
        variantParametersMap.clear();
        List<VariantParameter> variants = pLoader.getVariants();
        for (VariantParameter variant : variants) {
            Map<String, String> parameters = pICustomParameter.apply(variant);
            variantParametersMap.put(variant.getVariantName(), parameters);
        }
    }

    public static String getParameter(String pVariantName, String pParameterKey) {
        return variantParametersMap.getOrDefault(pVariantName, new HashMap<>()).getOrDefault(pParameterKey, "unknown");
    }
}