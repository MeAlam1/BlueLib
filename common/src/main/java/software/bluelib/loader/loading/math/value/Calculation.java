package software.bluelib.loader.loading.math.value;

import software.bluelib.loader.loading.math.MathValue;
import software.bluelib.loader.loading.math.Operator;


public final class Calculation implements MathValue {
    private final Operator operator;
    private final MathValue argA;
    private final MathValue argB;
    private final boolean isMutable;

    private double cachedValue = Double.MIN_VALUE;

    public Calculation(Operator operator, MathValue argA, MathValue argB) {
        this.operator = operator;
        this.argA = argA;
        this.argB = argB;
        this.isMutable = this.argA.isMutable() || this.argB.isMutable();
    }

    public Operator operator() {
        return this.operator;
    }

    public MathValue argA() {
        return this.argA;
    }

    public MathValue argB() {
        return this.argB;
    }

    @Override
    public double get() {
        if (this.isMutable)
            return this.operator.compute(this.argA.get(), this.argB.get());

        if (this.cachedValue == Double.MIN_VALUE)
            this.cachedValue = this.operator.compute(this.argA.get(), this.argB.get());

        return this.cachedValue;
    }

    @Override
    public boolean isMutable() {
        return this.isMutable;
    }

    @Override
    public String toString() {
        return this.argA.toString() + " " + this.operator.symbol() + " " + this.argB.toString();
    }
}
