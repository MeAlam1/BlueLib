// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class BlueLibConstants {

    private BlueLibConstants() {}

    public static final Logger LOGGER = Logger.getLogger(BlueLibConstants.MOD_NAME);

    public static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    public static final String MOD_ID = "bluelib";

    public static final String MOD_NAME = "BlueLib";

    public static boolean isBlueLibLoggingEnabled = true;

    public static boolean isLoggingEnabled = true;
}
