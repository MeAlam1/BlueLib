package software.bluelib.loader.loading.math.function.generic;

import net.minecraft.util.Mth;
import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.function.MathFunction;


public final class CosFunction extends MathFunction {
    private final MathValue value;

    public CosFunction(MathValue... values) {
        super(values);

        this.value = values[0];
    }

    @Override
    public String getName() {
        return "math.cos";
    }

    @Override
    public double compute() {
        return Mth.cos((float)this.value.get() * Mth.DEG_TO_RAD);
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
