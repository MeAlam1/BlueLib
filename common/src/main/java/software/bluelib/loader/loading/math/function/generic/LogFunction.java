package software.bluelib.loader.loading.math.function.generic;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class LogFunction extends MathFunction {
    private final MathValue value;

    public LogFunction(MathValue... values) {
        super(values);

        this.value = values[0];
    }

    @Override
    public String getName() {
        return "math.ln";
    }

    @Override
    public double compute() {
        return Math.log((float)this.value.get());
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
