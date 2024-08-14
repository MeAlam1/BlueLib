package software.bluelib.entity.variant;

import java.util.Map;

public interface ICustomParameter {
    Map<String, String> getCustomParameters(VariantKeys pVariant);
}
