/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
