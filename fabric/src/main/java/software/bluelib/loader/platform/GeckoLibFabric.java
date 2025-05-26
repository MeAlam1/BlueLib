package software.bluelib.loader.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import software.bluelib.loader.GeckoLibConstants;
import software.bluelib.loader.service.GeckoLibPlatform;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


public final class GeckoLibFabric implements GeckoLibPlatform {
    
    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    
    @Override
    public Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    
    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    
    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        final DataComponentType<T> componentType = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, GeckoLibConstants.id(id).toString(), builder.apply(DataComponentType.builder()).build());

        return () -> componentType;
    }
}
