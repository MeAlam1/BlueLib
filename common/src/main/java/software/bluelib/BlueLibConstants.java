package software.bluelib;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BlueLibConstants {

	/**
	 * A {@code public static final} {@link ScheduledExecutorService} used to schedule tasks, such as printing messages after a delay.
	 * <p>
	 * This executor runs tasks on a single thread to ensure delayed tasks run in a separate thread from the main thread.
	 * </p>
	 * @Co-author MeAlam, Dan
	 * @since 1.0.0
	 */
	public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

	/**
	 * A {@code public static final} {@link String} representing the Mod ID for the {@code BlueLib} mod.
	 * <p>This serves as a unique identifier for the mod.</p>
	 * @Co-author MeAlam, Dan
	 * @since 1.0.0
	 */
	public static final String MOD_ID = "bluelib";

	public static final String MOD_NAME = "BlueLib";
}