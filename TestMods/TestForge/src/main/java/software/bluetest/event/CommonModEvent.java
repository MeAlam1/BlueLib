package software.bluetest.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bluetest.BlueTest;
import software.bluetest.entity.dragon.DragonEntity;
import software.bluetest.entity.rex.RexEntity;
import software.bluetest.init.ModEntities;

@Mod.EventBusSubscriber(modid = BlueTest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvent {
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent pEvent) {
            pEvent.put(ModEntities.DRAGON.get(), DragonEntity.createAttributes().build());
            pEvent.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        }
    }
