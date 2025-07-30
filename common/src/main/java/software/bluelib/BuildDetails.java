package software.bluelib;

import org.jetbrains.annotations.NotNull;

public interface BuildDetails {
	@NotNull
	String getModId();

	@NotNull
	String getVersion();

	boolean displayWarning();
}