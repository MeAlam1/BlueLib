// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.init;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonRender;
import software.bluelib.example.entity.rex.RexRender;

/**
 * A {@code public class} that extends {@link ClientModInitializer} and contains the events that are fired on the client side.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #onInitializeClient()} - Registers the renderers for the entities.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class ClientInit implements ClientModInitializer {

    /**
     * A {@code public void} that registers the renderers for the entities.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public void onInitializeClient() {
        if (BlueLibCommon.isDeveloperMode() && BlueLibCommon.PLATFORM.isModLoaded("geckolib") && BlueLibConstants.isExampleEnabled) {
            EntityRendererRegistry.register(ModEntities.EXAMPLE_ONE, DragonRender::new);
            EntityRendererRegistry.register(ModEntities.EXAMPLE_TWO, RexRender::new);
        }
    }
}
