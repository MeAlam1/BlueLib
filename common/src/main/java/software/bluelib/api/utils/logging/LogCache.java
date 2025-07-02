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
import org.jetbrains.annotations.NotNull;

public class LogCache {

	@NotNull
	private static final List<LogEntry> logEntries = Collections.synchronizedList(new ArrayList<>());

	public static void addLog(@NotNull String pMessage, @NotNull Integer pColor) {
		logEntries.add(new LogEntry(pMessage, pColor));
	}

	@NotNull
	public static List<LogEntry> getLogs() {
		return new ArrayList<>(logEntries);
	}

	public static void clearLogs() {
		logEntries.clear();
	}

	public record LogEntry(@NotNull String message, @NotNull Integer color) {}
}
