package software.bluelib.loader.loading.math.function.round;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class RoundFunction extends MathFunction {
    private final MathValue value;

    public RoundFunction(MathValue... values) {
        super(values);

        this.value = values[0];
    }

    @Override
    public String getName() {
        return "math.round";
    }

    @Override
    public double compute() {
        return Math.round(this.value.get());
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
