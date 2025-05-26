package software.bluelib_examples.variant;

import software.bluelib.api.entity.variant.IVariantProvider;

import java.util.List;

public class VariantProvider implements IVariantProvider {
	@Override
	public List<String> getEntityNames() {
		return List.of("example");
	}
}
