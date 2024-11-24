// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.example.event.ClientEvents;
import software.bluelib.example.init.ModEntities;

/**
 * The main class of the {@code BlueLib} mod.
 * <p>
 * This class serves as the entry point for the {@code BlueLib} mod, handling initialization by registering event handlers
 * and setting up necessary configurations. For more details, refer to the <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #BlueLib(IEventBus, ModContainer)} - Constructs the {@code BlueLib} instance and registers the mod event bus.</li>
 * <li>{@link #onLoadComplete(FMLLoadCompleteEvent)} - Handles the event when the mod loading is complete.</li>
 * </ul>
 *
 * @author MeAlam, Dan and All Contributors of BlueLib!
 * @version 1.0.0
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>
 * @since 1.0.0
 */
@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    /**
     * Constructs a new {@code BlueLib} instance and registers the mod event bus.
     * <p>
     * Registers necessary mod event listeners, and if in developer mode, additional client-side listeners for rendering and attributes.
     * </p>
     *
     * @param pModEventBus  {@link IEventBus} - The event bus where the mod registers its handlers.
     * @param pModContainer {@link ModContainer} - The mod container that holds the instance of the mod.
     * @author MeAlam
     * @since 1.0.0
     */
    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        pModEventBus.register(this);
        MixinBootstrap.init();
        if (BlueLibCommon.isDeveloperMode() && BlueLibCommon.PLATFORM.isModLoaded("geckolib") && BlueLibConstants.isExampleEnabled) {
            ModEntities.REGISTRY.register(pModEventBus);
            if (FMLEnvironment.dist.isClient()) {
                pModEventBus.addListener(ClientEvents::registerAttributes);
                pModEventBus.addListener(ClientEvents::registerRenderers);
            }
        }
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
