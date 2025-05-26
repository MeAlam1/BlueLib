package software.bluelib.loader.loading.math.function;

import software.bluelib.loader.loading.math.MathValue;

import java.util.StringJoiner;


public abstract class MathFunction implements MathValue {
    private final boolean isMutable;
    private double cachedValue = Double.MIN_VALUE;

    protected MathFunction(MathValue... values) {
        validate(values);

        this.isMutable = isMutable(values);
    }

    
    public abstract String getName();

    @Override
    public final double get() {
        if (this.isMutable)
            return compute();

        if (this.cachedValue == Double.MIN_VALUE)
            this.cachedValue = compute();

        return this.cachedValue;
    }

    
    public abstract double compute();

    
    public boolean isMutable(MathValue... values) {
        for (MathValue value : values) {
            if (value.isMutable())
                return true;
        }

        return false;
    }

    
    public abstract int getMinArgs();

    
    public abstract MathValue[] getArgs();

    
    public void validate(MathValue... inputs) throws IllegalArgumentException {
        final int minArgs = getMinArgs();

        if (inputs.length < minArgs)
            throw new IllegalArgumentException(String.format("Function '%s' at least %s arguments. Only %s given!", getName(), minArgs, inputs.length));
    }

    @Override
    public final boolean isMutable() {
        return this.isMutable;
    }

    @Override
    public String toString() {
        final MathValue[] args = getArgs();
        final StringJoiner joiner = new StringJoiner(", ", "(", ")");

        for (MathValue arg : args) {
            joiner.add(arg.toString());
        }

        return getName() + joiner;
    }

    
    @FunctionalInterface
    public interface Factory<T extends MathFunction> {
        
        T create(MathValue... values);
    }
}
