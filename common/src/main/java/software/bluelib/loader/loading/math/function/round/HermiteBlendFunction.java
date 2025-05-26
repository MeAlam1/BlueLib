package software.bluelib.loader.loading.math.function.round;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class HermiteBlendFunction extends MathFunction {
    private final MathValue valueA;

    public HermiteBlendFunction(MathValue... values) {
        super(values);

        this.valueA = values[0];
    }

    @Override
    public String getName() {
        return "math.hermite_blend";
    }

    @Override
    public double compute() {
        final double value = this.valueA.get();

        return (3 * value * value) - (2 * value * value * value);
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public MathValue[] getArgs() {
        return new MathValue[] {this.valueA};
    }
}
