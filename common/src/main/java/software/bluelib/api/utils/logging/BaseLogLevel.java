/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.logging;

import java.util.logging.Level;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
public class BaseLogLevel {

    public static final Level INFO = new Level("INFO", Level.INFO.intValue()) {};

    public static final Level ERROR = new Level("ERROR", Level.SEVERE.intValue()) {};

    public static final Level WARNING = new Level("WARNING", Level.WARNING.intValue()) {};

    public static final Level SUCCESS = new Level("SUCCESS", Level.INFO.intValue() + 50) {};

    @ApiStatus.Internal
    public static final Level BLUELIB = new Level("BlueLib Developer", Level.INFO.intValue() + 50) {};
}
