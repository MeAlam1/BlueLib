package software.bluelib_examples;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import software.bluelib.event.ReloadHandler;
import software.bluelib_examples.registry.EntityRegistry;
import software.bluelib_examples.variant.VariantProvider;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, BlueLibConstants.MOD_ID);

    public BlueLib(IEventBus pModEventBus) {
        ReloadHandler.registerProvider(new VariantProvider());
        ENTITIES.register(pModEventBus);
        pModEventBus.<EntityAttributeCreationEvent>addListener(event -> EntityRegistry.registerEntityAttributes(event::put));

        BlueLibCommon.doServerRegistration();
    }
}
