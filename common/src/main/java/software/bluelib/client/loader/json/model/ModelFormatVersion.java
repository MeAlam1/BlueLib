package software.bluelib.client.loader.json.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class ModelFormatVersion {
	private static final Map<String, ModelFormatVersion> REGISTRY = new Object2ObjectOpenHashMap<>();

	static {
		register("1.12.0");
		register("1.14.0");
		register("1.21.0");
	}

	private final String serializedName;
	private final boolean supported;
	private final String errorMessage;

	protected ModelFormatVersion(String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
		this.serializedName = pSerializedName;
		this.supported = pSupported;
		this.errorMessage = pErrorMessage;
	}

	protected static ModelFormatVersion register(String pName) {
		return register(pName, true, null);
	}

	protected static ModelFormatVersion register(String pName, boolean pSupported, @Nullable String pErrorMessage) {
		ModelFormatVersion version = new ModelFormatVersion(pName, pSupported, pErrorMessage);
		REGISTRY.put(pName, version);
		return version;
	}

	@Nullable
	public static ModelFormatVersion match(String pVersion) {
		return REGISTRY.get(pVersion);
	}

	@Nullable
	public static ModelFormatVersion match(ModelFormatVersion pVersion) {
		return REGISTRY.get(pVersion.serializedName);
	}

	public String getSerializedName() {
		return serializedName;
	}

	public boolean isSupported() {
		return supported;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public static Map<String, ModelFormatVersion> getRegisteredVersions() {
		return Collections.unmodifiableMap(REGISTRY);
	}
}
