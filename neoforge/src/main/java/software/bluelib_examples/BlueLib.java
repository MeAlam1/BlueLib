package software.bluelib_examples;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import software.bluelib.client.BlueLibClient;
import software.bluelib.event.ReloadHandler;
import software.bluelib_examples.registry.EntityRegistry;
import software.bluelib_examples.variant.VariantProvider;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, BlueLibConstants.MOD_ID);

    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        if (FMLEnvironment.dist == Dist.CLIENT)
            BlueLibClient.init(pModContainer);

        BlueLibCommon.doServerRegistration();

        ReloadHandler.registerProvider(new VariantProvider());
        ENTITIES.register(pModEventBus);
        pModEventBus.<EntityAttributeCreationEvent>addListener(event -> EntityRegistry.registerEntityAttributes(event::put));
    }
}
