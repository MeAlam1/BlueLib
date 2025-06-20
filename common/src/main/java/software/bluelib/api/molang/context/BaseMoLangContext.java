package software.bluelib.api.molang.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import software.bluelib.api.molang.MoLangContext;
import software.bluelib.api.molang.MoLangRuntime;

public abstract class BaseMoLangContext implements MoLangContext {

    protected final Map<String, Object> variables = new HashMap<>();
    protected final Map<String, BiFunction<List<Object>, MoLangRuntime, Object>> functions = new HashMap<>();

    public void registerFunction(String pName, BiFunction<List<Object>, MoLangRuntime, Object> pFunction) {
        functions.put(pName, pFunction);
    }

    public void setVariable(String pName, Supplier<?> pSupplier) {
        variables.put(pName, pSupplier);
    }

    public void setVariable(String pName, Object pSupplier) {
        variables.put(pName, pSupplier instanceof Supplier<?> ? pSupplier : (Supplier<?>) () -> pSupplier);
    }

    @Override
    public Object getVariable(String pName) {
        Supplier<?> supplier = (Supplier<?>) variables.get(pName);
        return supplier != null ? supplier.get() : null;
    }

    @Override
    public Object callFunction(String pName, List<Object> pArguments) {
        var fn = functions.get(pName);
        return fn != null ? fn.apply(pArguments, null) : null;
    }
}
