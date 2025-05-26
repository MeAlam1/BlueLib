package software.bluelib.loader.loading.math.function.generic;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class ModFunction extends MathFunction {
    private final MathValue value;
    private final MathValue modulus;

    public ModFunction(MathValue... values) {
        super(values);

        this.value = values[0];
        this.modulus = values[1];
    }

    @Override
    public String getName() {
        return "math.mod";
    }

    @Override
    public double compute() {
        return this.value.get() % this.modulus.get();
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public MathValue[] getArgs() {
        return new MathValue[] {this.value, this.modulus};
    }
}
