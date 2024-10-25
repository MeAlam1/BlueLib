// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.proxy;

import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLibConstants;

/**
 * A {@code public class} that is annotated with {@link Mod.EventBusSubscriber} to handle common events.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #postInit()} - Handles the event after the initialization of the mod.</li>
 *   <li>{@link #clientInit()} - Handles the event after the initialization of the client.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
@Mod.EventBusSubscriber(modid = BlueLibConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    /**
     * A {@code public void} method that is called after the initialization of the mod.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public void postInit() {
    }

    /**
     * A {@code public void} method that is called after the initialization of the client.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public void clientInit() {
    }
}
