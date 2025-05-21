

package software.bluelib_examples;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import software.bluelib_examples.registry.EntityRegistry;

public class BlueLib implements ModInitializer {

    @Override
    public void onInitialize() {
        BlueLibCommon.doRegistrations();
        EntityRegistry.registerEntityAttributes(FabricDefaultAttributeRegistry::register);
    }
}
