package software.bluelib.loader.loading.math.value;

import software.bluelib.loader.loading.math.MathValue;

import java.util.StringJoiner;


public record CompoundValue(MathValue[] subValues) implements MathValue {
    @Override
    public double get() {
        for (int i = 0; i < this.subValues.length - 1; i++) {
            this.subValues[i].get();
        }

        return this.subValues[this.subValues.length - 1].get();
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner("; ");

        for (MathValue subValue : this.subValues) {
            joiner.add(subValue.toString());
        }

        return joiner.toString();
    }
}
