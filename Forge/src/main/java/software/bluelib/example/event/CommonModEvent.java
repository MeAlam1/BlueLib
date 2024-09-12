package software.bluelib.example.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLib;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.rex.RexEntity;
import software.bluelib.example.init.ModEntities;

@Mod.EventBusSubscriber(modid = BlueLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvent {
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent pEvent) {
        if (BlueLib.EditorMode) {
            pEvent.put(ModEntities.DRAGON.get(), DragonEntity.createAttributes().build());
            pEvent.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        }
    }
}
