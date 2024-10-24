// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;
import software.bluelib.example.init.ModEntities;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} that contains the events that are fired on the common side.
 * <p>
 * Key Methods:
 * <ul>
 *     <li>{@link #onAttributeCreate(EntityAttributeCreationEvent)} - Registers the attributes for the entities.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
@Mod.EventBusSubscriber(modid = BlueLibConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvent {

    /**
     * A {@code public static void} that registers the attributes for the entities.
     *
     * @param pEvent {@link EntityAttributeCreationEvent} - The event that is fired when the attributes are being registered.
     * @author MeAlam
     * @since 1.0.0
     */
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent pEvent) {
        pEvent.put(ModEntities.DRAGON.get(), DragonEntity.createAttributes().build());
        pEvent.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        BaseLogger.log(BaseLogLevel.INFO, "Registered Attributes for Entities", true);
    }
}
