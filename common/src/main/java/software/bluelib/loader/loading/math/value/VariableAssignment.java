package software.bluelib.loader.loading.math.value;

import software.bluelib.loader.loading.math.MathValue;


public record VariableAssignment(Variable variable, MathValue value) implements MathValue {
    @Override
    public double get() {
        this.variable.set(this.value.get());

        return 0;
    }

    @Override
    public String toString() {
        return this.variable.name() + "=" + this.value.toString();
    }
}
