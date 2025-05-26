package software.bluelib.loader.loading.math.function.limit;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class MinFunction extends MathFunction {
    private final MathValue valueA;
    private final MathValue valueB;

    public MinFunction(MathValue... values) {
        super(values);

        this.valueA = values[0];
        this.valueB = values[1];
    }

    @Override
    public String getName() {
        return "math.min";
    }

    @Override
    public double compute() {
        return Math.min(this.valueA.get(), this.valueB.get());
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public MathValue[] getArgs() {
        return new MathValue[] {this.valueA, this.valueB};
    }
}
