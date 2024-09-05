// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib3.GeckoLib;
import software.bluelib.example.event.ReloadHandler;
import software.bluelib.example.init.ModEntities;
import software.bluelib.example.proxy.ClientProxy;
import software.bluelib.example.proxy.CommonProxy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main class of the {@link BlueLib} mod.
 * <p>
 * This class serves as the entry point for the {@link BlueLib} mod, handling initialization by registering event handlers
 * and setting up necessary configurations. For more details, refer to the <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #BlueLib()} - Constructs the {@link BlueLib} instance and registers the mod event bus.</li>
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
     * A {@link ScheduledExecutorService} used for scheduling tasks, such as printing messages after a delay.
     * <p>
     * This is initialized with a single-threaded pool to handle delayed tasks in a separate thread.
     * </p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * The Mod ID for {@link BlueLib}. This serves as a unique identifier for the mod.
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    public static final String MODID = "bluelib";

    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    // public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructs a new {@link BlueLib} instance and registers the mod event bus.
     *
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public BlueLib()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);

        if (isDeveloperMode()) {
            ModEntities.register(modEventBus);
            GeckoLib.initialize();
            MinecraftForge.EVENT_BUS.register(ReloadHandler.class);
            modEventBus.addListener(this::setupComplete);
            modEventBus.addListener(this::setupClient);
        }
    }

    private void setupClient(final FMLClientSetupEvent pEvent) {
        PROXY.clientInit();
    }

    private void setupComplete(final FMLLoadCompleteEvent pEvent) {
        PROXY.postInit();
    }

    /**
     * Handles the {@link FMLLoadCompleteEvent}, which is triggered when the mod loading process is complete.
     * <p>
     * If the mod is running in developer mode, it schedules a task to print a thank-you message to the console after a short delay.
     * </p>
     *
     * @param pEvent {@link FMLLoadCompleteEvent} - The event triggered upon the completion of the mod loading process.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        if (isDeveloperMode()) {
            scheduler.schedule(() -> {
                System.out.println(
                        "\n" +
                        "**************************************************\n" +
                        "*                                                *\n" +
                        "*    Thank you for using BlueLib!                *\n" +
                        "*    We appreciate your support.                 *\n" +
                        "*                                                *\n" +
                        "**************************************************"
                );

                scheduler.shutdown();
            }, 5, TimeUnit.SECONDS);
        }
    }

    /**
     * Checks if the mod is running in developer mode.
     * <p>
     * Developer mode is determined by checking if the mod is not running in a production environment.
     * </p>
     *
     * @return {@code true} if the mod is running in developer mode, {@code false} otherwise.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    static boolean isDeveloperMode() {
        return !FMLEnvironment.production;
    }
}
