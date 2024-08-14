package software.bluelib.interfaces.variant;

import software.bluelib.entity.variant.VariantParameter;

import java.util.Map;

public interface ICustomParameter {
    Map<String, String> getCustomParameters(VariantParameter pVariant);
}
