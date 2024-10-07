// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.concurrent.TimeUnit;

import static software.bluelib.BlueLibConstants.SCHEDULER;

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
 *   <li>{@link #onLoadComplete(FMLLoadCompleteEvent)} - Handles the event when the mod loading is complete.</li>
 * </ul>
 * </p>
 *
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>
 * @author MeAlam, Dan
 * @co-author All Contributors of BlueLib!
 * @since 1.0.0
 */
@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    /**
     * Constructs a new {@link BlueLib} instance and registers the mod event bus.
     *
     * @author MeAlam
     * @co-author Dan
     * @since 1.0.0
     */
    public BlueLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
    }

    /**
     * A {@code public void} that handles the {@link FMLLoadCompleteEvent}, which occurs when the mod finishes loading.
     *
     * @param pEvent {@link FMLLoadCompleteEvent} - The event fired after the mod loading process completes.
     * @author MeAlam
     * @co-author Dan
     * @since 1.0.0
     */
    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        BlueLibCommon.init();
    }
}
