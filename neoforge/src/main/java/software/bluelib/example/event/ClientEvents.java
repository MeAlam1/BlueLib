// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.dragon.DragonRender;
import software.bluelib.example.entity.rex.RexEntity;
import software.bluelib.example.entity.rex.RexRender;
import software.bluelib.example.init.ModEntities;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} that contains the events that are fired on the client side.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #registerRenderers(EntityRenderersEvent.RegisterRenderers)} - Registers the renderers for the entities.</li>
 * <li>{@link #registerAttributes(EntityAttributeCreationEvent)} - Registers the attributes for the entities.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class ClientEvents {

    /**
     * A {@code public static void} that registers the renderers for the entities.
     *
     * @param pEvent {@link EntityRenderersEvent} - The event that is fired when the renderers are being registered.
     * @author MeAlam
     * @since 1.0.0
     */
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers pEvent) {
        pEvent.registerEntityRenderer(ModEntities.DRAGON.get(), DragonRender::new);
        pEvent.registerEntityRenderer(ModEntities.REX.get(), RexRender::new);
        BaseLogger.log(BaseLogLevel.INFO, "Registered Renderers for Entities", true);
    }

    /**
     * A {@code public static void} that registers the attributes for the entities.
     *
     * @param pEvent {@link EntityAttributeCreationEvent} - The event that is fired when the attributes are being registered.
     * @author MeAlam
     * @since 1.0.0
     */
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent pEvent) {
        pEvent.put(ModEntities.DRAGON.get(), DragonEntity.createAttributes().build());
        pEvent.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        BaseLogger.log(BaseLogLevel.INFO, "Registered Attributes for Entities", true);
    }
}
