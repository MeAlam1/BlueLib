// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import software.bluelib.example.event.ClientEvents;
import software.bluelib.example.init.ModEntities;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main class of the {@link BlueLib} mod.
 * <p>
 * This class serves as the entry point for the {@link BlueLib} mod, handling initialization by registering event handlers
 * and setting up necessary configurations. For more details, refer to the <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #BlueLib(IEventBus, ModContainer)} - Constructs the {@link BlueLib} instance and registers the mod event bus.</li>
 *   <li>{@link #onLoadComplete(FMLLoadCompleteEvent)} - Handles the event when the mod loading is complete and prints a thank-you message if in developer mode.</li>
 *   <li>{@link #isDeveloperMode()} - Determines if the mod is running in developer mode.</li>
 * </ul>
 * </p>
 *
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>
 * @author MeAlam, Dan
 * @Co-author All Contributors of BlueLib!
 * @since 1.0.0
 */
@Mod(BlueLib.MODID)
public class BlueLib {

    /**
     * A {@link ScheduledExecutorService} used to schedule tasks, such as printing messages after a delay.
     * <p>
     * This executor runs tasks on a single thread to ensure delayed tasks run in a separate thread from the main thread.
     * </p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    /**
     * A {@link String} representing the Mod ID for the {@link BlueLib} mod.
     * <p>This serves as a unique identifier for the mod.</p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    public static final String MODID = "bluelib";

    /**
     * Constructs a new {@link BlueLib} instance and registers the mod event bus.
     * <p>
     * Registers necessary mod event listeners, and if in developer mode, additional client-side listeners for rendering and attributes.
     * </p>
     *
     * @param pModEventBus {@link IEventBus} - The event bus where the mod registers its handlers.
     * @param pModContainer {@link ModContainer} - The mod container that holds the instance of the mod.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        pModEventBus.register(this);
        MixinBootstrap.init();
        if (isDeveloperMode()) {
            ModEntities.REGISTRY.register(pModEventBus);
            if (FMLEnvironment.dist.isClient()) {
                pModEventBus.addListener(ClientEvents::registerAttributes);
                pModEventBus.addListener(ClientEvents::registerRenderers);
            }
        }
    }

    /**
     * a {@code void} that handles the {@link FMLLoadCompleteEvent}, which occurs when the mod finishes loading.
     * <p>
     * If the mod is in developer mode, it schedules a task that prints a thank-you message after a short delay.
     * </p>
     *
     * @param pEvent {@link FMLLoadCompleteEvent} - The event fired after the mod loading process completes.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        if (isDeveloperMode()) {
            SCHEDULER.schedule(() -> {
                BaseLogger.logBlueLib("**************************************************");
                BaseLogger.logBlueLib("                                                  ");
                BaseLogger.logBlueLib("     Thank you for using BlueLib!                 ");
                BaseLogger.logBlueLib("     We appreciate your support.                  ");
                BaseLogger.logBlueLib("                                                  ");
                BaseLogger.logBlueLib("**************************************************");
                SCHEDULER.shutdown();
            }, 3, TimeUnit.SECONDS);
        }
    }

    /**
     * a {@code void} that checks if the mod is running in developer mode.
     * <p>
     * Developer mode is active when the mod is not running in a production environment.
     * </p>
     *
     * @return {@code true} if running in developer mode, {@code false} otherwise.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    static boolean isDeveloperMode() {
        boolean isDevMode = !FMLEnvironment.production;
        if (isDevMode) {
            BaseLogger.log(BaseLogLevel.INFO ,"Running in Developer mode.", true);
        } else {
            BaseLogger.log(BaseLogLevel.INFO ,"Running in Production mode.", true);
        }
        return isDevMode;
    }
}
