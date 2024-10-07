// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
 * @author MeAlam, Dan and All Contributors of BlueLib!
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>
 * @since 1.0.0
 */
@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    /**
     * Constructs a new {@link BlueLib} instance and registers the mod event bus.
     *
     * @author MeAlam
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
     * @since 1.0.0
     */
    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent pEvent) {
        BlueLibCommon.init();
    }
}
