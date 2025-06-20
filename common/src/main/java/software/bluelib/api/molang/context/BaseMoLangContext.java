package software.bluelib.api.molang.context;

import software.bluelib.api.molang.MoLangContext;
import software.bluelib.api.molang.MoLangRuntime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class BaseMoLangContext implements MoLangContext {

	protected final Map<String, Object> variables = new HashMap<>();
	protected final Map<String, BiFunction<List<Object>, MoLangRuntime, Object>> functions = new HashMap<>();

	public void registerFunction(String pName, BiFunction<List<Object>, MoLangRuntime, Object> pFunction) {
		functions.put(pName, pFunction);
	}

	public void setVariable(String pName, Supplier<?> pSupplier) {
		variables.put(pName, pSupplier);
	}

	public void setVariable(String pName, Object pValue) {
		variables.put(pName, pValue);
	}

	@Override
	public Object getVariable(String pName) {
		Object value = variables.get(pName);
		if (value instanceof Supplier<?> supplier) {
			return supplier.get();
		}
		return value;
	}

	@Override
	public Object callFunction(String pName, List<Object> pArguments) {
		var fn = functions.get(pName);
		return fn != null ? fn.apply(pArguments, null) : null;
	}
}
