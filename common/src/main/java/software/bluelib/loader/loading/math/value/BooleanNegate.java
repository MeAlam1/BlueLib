package software.bluelib.loader.loading.math.value;

import software.bluelib.loader.loading.math.MathValue;


public record BooleanNegate(MathValue value) implements MathValue {
    @Override
    public double get() {
        return this.value.get() == 0 ? 1 : 0;
    }

    @Override
    public boolean isMutable() {
        return this.value.isMutable();
    }

    @Override
    public String toString() {
        return "!" + this.value.toString();
    }
}
