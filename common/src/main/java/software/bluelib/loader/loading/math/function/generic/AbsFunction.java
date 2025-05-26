package software.bluelib.loader.loading.math.function.generic;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class AbsFunction extends MathFunction {
    private final MathValue value;

    public AbsFunction(MathValue... values) {
        super(values);

        this.value = values[0];
    }

    @Override
    public String getName() {
        return "math.abs";
    }

    @Override
    public double compute() {
        return Math.abs(this.value.get());
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public MathValue[] getArgs() {
        return new MathValue[] {this.value};
    }
}
