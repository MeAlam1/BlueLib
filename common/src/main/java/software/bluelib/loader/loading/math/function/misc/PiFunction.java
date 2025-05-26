package software.bluelib.loader.loading.math.function.misc;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;
import software.bluelib.loader.loading.math.value.Constant;


public final class PiFunction extends MathFunction {
    public PiFunction(MathValue... values) {
        super(values);
    }

    @Override
    public String getName() {
        return "math.pi";
    }

    @Override
    public double compute() {
        return Math.PI;
    }

    @Override
    public boolean isMutable(MathValue... values) {
        return false;
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public MathValue[] getArgs() {
        return new MathValue[] {new Constant(Math.PI)};
    }
}
