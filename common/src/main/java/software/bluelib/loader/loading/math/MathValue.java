package software.bluelib.loader.loading.math;

import org.jetbrains.annotations.ApiStatus;

import java.util.function.DoubleSupplier;


public interface MathValue extends DoubleSupplier {
    
    double get();

    
    default boolean isMutable() {
        return true;
    }

    
    @ApiStatus.Internal
    @Override
    default double getAsDouble() {
        return get();
    }
}
