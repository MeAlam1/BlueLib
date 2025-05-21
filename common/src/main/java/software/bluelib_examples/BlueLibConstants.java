

package software.bluelib_examples;

import software.bluelib_examples.platform.IPlatform;

import java.util.ServiceLoader;

public class BlueLibConstants {

    public static final String MOD_ID = "bluelib_examples";

    public static final String MOD_NAME = "BlueLib";

    public static final IPlatform PLATFORM = ServiceLoader.load(IPlatform.class).findFirst().orElseThrow();

}
