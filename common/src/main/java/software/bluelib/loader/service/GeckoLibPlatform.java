package software.bluelib.loader.service;

import net.minecraft.core.component.DataComponentType;

import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


public interface GeckoLibPlatform {
    
    boolean isDevelopmentEnvironment();

    
    boolean isPhysicalClient();

    
    Path getGameDir();

    
    <T> Supplier<DataComponentType<T>> registerDataComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builder);
}
