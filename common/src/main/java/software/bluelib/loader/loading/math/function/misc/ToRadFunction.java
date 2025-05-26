package software.bluelib.loader.loading.math.function.misc;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class ToRadFunction extends MathFunction {
    private final MathValue value;

    public ToRadFunction(MathValue... values) {
        super(values);

        this.value = values[0];
    }

    @Override
    public String getName() {
        return "math.to_rad";
    }

    @Override
    public double compute() {
        return Math.toRadians(this.value.get());
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
