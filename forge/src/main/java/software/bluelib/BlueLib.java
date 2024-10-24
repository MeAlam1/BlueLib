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
import software.bluelib.example.event.ReloadHandler;
import software.bluelib.example.init.ModEntities;
import software.bluelib.example.proxy.ClientProxy;
import software.bluelib.example.proxy.CommonProxy;

/**
 * The main class of the {@link BlueLib} mod.
 * <p>
 * This class serves as the entry point for the {@link BlueLib} mod, handling initialization by registering event handlers
 * and setting up necessary configurations. For more details, refer to the <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #BlueLib()} - Constructs the {@link BlueLib} instance and registers the mod event bus.</li>
 *   <li>{@link #onLoadComplete(FMLLoadCompleteEvent)} - Handles the event when the mod loading is complete.</li>
 * </ul>
 *
 * @author MeAlam
 * @see <a href="https://github.com/MeAlam1/BlueLib/wiki">BlueLib Wiki</a>
 * @since 1.0.0
 */
@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    /**
     * A {@code public static} {@link CommonProxy} instance that handles the mod's initialization and event handling.
     */
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    /**
     * Constructs a new {@link BlueLib} instance and registers the mod event bus.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public BlueLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);

        if (BlueLibCommon.isDeveloperMode() && BlueLibCommon.PLATFORM.isModLoaded("geckolib") && BlueLibConstants.isExampleEnabled) {
            ModEntities.register(modEventBus);
            MinecraftForge.EVENT_BUS.register(ReloadHandler.class);
            modEventBus.addListener(this::setupComplete);
            modEventBus.addListener(this::setupClient);
        }
    }

    /**
     * A {@code private void} that handles the {@link FMLClientSetupEvent}. <br>
     * Once the client setup process is complete, this method calls {@link ClientProxy#clientInit()} to perform additional client-side initialization.
     *
     * @param pEvent {@link FMLClientSetupEvent} - The event fired after the client setup process completes.
     */
    private void setupClient(final FMLClientSetupEvent pEvent) {
        pEvent.enqueueWork(() -> {
            PROXY.clientInit();
        });
    }

    /**
     * A {@code private void} that handles the {@link FMLLoadCompleteEvent}. <br>
     * Once the mod loading process is complete, this method calls {@link CommonProxy#postInit()} to perform additional initialization.
     *
     * @param pEvent {@link FMLLoadCompleteEvent} - The event fired after the mod loading process completes.
     */
    private void setupComplete(final FMLLoadCompleteEvent pEvent) {
        PROXY.postInit();
    }

    /**
     * A {@code public void} method that handles the {@link FMLLoadCompleteEvent}, which is triggered once
     * the mod loading process is complete.
     * <p>
     * This method calls {@link BlueLibCommon#init()} to perform additional initialization after all mod-related
     * loading steps are finished.
     * </p>
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
