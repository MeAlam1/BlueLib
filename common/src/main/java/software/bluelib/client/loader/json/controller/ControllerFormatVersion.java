package software.bluelib.client.loader.json.controller;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.loader.json.FormatVersion;

import java.util.Map;

public class ControllerFormatVersion extends FormatVersion<ControllerFormatVersion> {
	private static final Map<String, ControllerFormatVersion> REGISTRY = new Object2ObjectOpenHashMap<>();

	static {
		register("1.0.0");
	}

	protected ControllerFormatVersion(String pSerializedName, boolean pSupported, @Nullable String pErrorMessage) {
		super(pSerializedName, pSupported, pErrorMessage);
	}

	protected static ControllerFormatVersion register(String pName) {
		return FormatVersion.register(REGISTRY, new ControllerFormatVersion(pName, true, null));
	}

	protected static ControllerFormatVersion register(String pName, boolean pSupported, @Nullable String pErrorMessage) {
		return FormatVersion.register(REGISTRY, new ControllerFormatVersion(pName, pSupported, pErrorMessage));
	}

	@Nullable
	public static ControllerFormatVersion match(@Nullable String pVersion) {
		return FormatVersion.match(REGISTRY, pVersion);
	}

	public static Map<String, ControllerFormatVersion> getRegisteredVersions() {
		return FormatVersion.getRegisteredVersions(REGISTRY);
	}
}