// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogCache {

    private static final List<LogEntry> logEntries = Collections.synchronizedList(new ArrayList<>());

    public static void addLog(String pMessage, int pColor) {
        logEntries.add(new LogEntry(pMessage, pColor));
    }

    public static List<LogEntry> getLogs() {
        return new ArrayList<>(logEntries);
    }

    public static void clearLogs() {
        logEntries.clear();
    }

    public record LogEntry(String message, int color) {}
}
