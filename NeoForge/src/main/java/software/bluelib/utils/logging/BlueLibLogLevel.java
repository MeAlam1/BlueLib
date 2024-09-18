package software.bluelib.utils.logging;

import java.util.logging.Level;

public class BlueLibLogLevel {
    public static final Level INFO = new Level("INFO: ", Level.INFO.intValue()) {};
    public static final Level ERROR = new Level("ERROR: ", Level.SEVERE.intValue()) {};
    public static final Level WARNING = new Level("WARNING: ", Level.WARNING.intValue()) {};

    public static final Level SUCCESS = new Level("SUCCESS: ", Level.INFO.intValue() + 50) {};
    public static final Level BLUELIB = new Level("", Level.INFO.intValue() + 50) {};
}
