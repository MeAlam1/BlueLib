package software.bluelib_examples.variant;

import java.util.List;
import software.bluelib.api.entity.variant.IVariantProvider;

public class VariantProvider implements IVariantProvider {

    @Override
    public List<String> getEntityNames() {
        return List.of("example");
    }
}
