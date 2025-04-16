// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.platform;

@SuppressWarnings("unused")
public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String pModId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
