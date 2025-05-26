package software.bluelib.loader.loading.math.function.misc;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class ToDegFunction extends MathFunction {
    private final MathValue value;

    public ToDegFunction(MathValue... values) {
        super(values);

        this.value = values[0];
    }

    @Override
    public String getName() {
        return "math.to_deg";
    }

    @Override
    public double compute() {
        return Math.toDegrees(this.value.get());
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
