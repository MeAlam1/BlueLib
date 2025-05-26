package software.bluelib.loader.platform;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import software.bluelib.loader.GeckoLib;
import software.bluelib.loader.service.GeckoLibPlatform;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


public final class GeckoLibNeoForge implements GeckoLibPlatform {
    
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    
    @Override
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    
    @Override
    public boolean isPhysicalClient() {
        return FMLEnvironment.dist.isClient();
    }

    
    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return GeckoLib.DATA_COMPONENTS_REGISTER.registerComponentType(id, builder);
    }
}
