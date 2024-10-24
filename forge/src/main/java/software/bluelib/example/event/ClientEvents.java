// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonRender;
import software.bluelib.example.entity.rex.RexRender;
import software.bluelib.example.init.ModEntities;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} that contains the events that are fired on the client side.
 * <p>
 * Key Methods:
 * <ul>
 *     <li>{@link #registerRenderers()} - Registers the renderers for the entities.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
@Mod.EventBusSubscriber(modid = BlueLibConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    /**
     * A {@code public static void} that registers the renderers for the entities.
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public static void registerRenderers() {
        EntityRenderers.register(ModEntities.DRAGON.get(), DragonRender::new);
        EntityRenderers.register(ModEntities.REX.get(), RexRender::new);
        BaseLogger.log(BaseLogLevel.INFO, "Registered Renderers for Entities", true);
    }
}
