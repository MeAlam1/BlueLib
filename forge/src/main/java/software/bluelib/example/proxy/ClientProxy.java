// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.event.ClientEvents;

/**
 * A {@code public class} that extends {@link CommonProxy} and is annotated with {@link Mod.EventBusSubscriber} to handle
 * client-side events.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #clientInit()} - Handles the event after the initialization of the client.</li>
 * </ul>
 *
 * @author MeAlam
 * @see CommonProxy
 * @since 1.0.0
 */
@Mod.EventBusSubscriber(modid = BlueLibConstants.MOD_ID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    /**
     * A {@code public void} method that is called after the initialization of the client.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public void clientInit() {
        super.clientInit();
        ClientEvents.registerRenderers();
    }
}
