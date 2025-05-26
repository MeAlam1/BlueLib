package software.bluelib.loader.loading.math.value;

import software.bluelib.loader.loading.math.MathValue;


public record Negative(MathValue value) implements MathValue {
    @Override
    public double get() {
        return -this.value.get();
    }

    @Override
    public boolean isMutable() {
        return this.value.isMutable();
    }

    @Override
    public String toString() {
        if (this.value instanceof Constant)
            return "-" + this.value;

        return "-" + "(" + this.value + ")";
    }
}
