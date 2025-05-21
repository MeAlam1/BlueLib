package software.bluelib.api.entity.variant;

import java.util.List;

public interface IVariantProvider {
	List<String> getEntityNames();
	
	default String getBasePath() {
		return "variant/entity/";
	}
}
