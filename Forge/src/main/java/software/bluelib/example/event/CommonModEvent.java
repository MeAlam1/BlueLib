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

@Mod.EventBusSubscriber(modid = BlueLibConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvent {
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent pEvent) {
        pEvent.put(ModEntities.DRAGON.get(), DragonEntity.createAttributes().build());
        pEvent.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        BaseLogger.log(BaseLogLevel.INFO, "Registered Attributes for Entities", true);
    }
}
