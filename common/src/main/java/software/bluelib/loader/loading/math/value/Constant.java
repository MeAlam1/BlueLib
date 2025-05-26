package software.bluelib.loader.loading.math.value;

import software.bluelib.loader.loading.math.MathValue;


public record Constant(double value) implements MathValue {
    @Override
    public double get() {
        return this.value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
