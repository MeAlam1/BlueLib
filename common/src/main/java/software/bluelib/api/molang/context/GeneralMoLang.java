package software.bluelib.api.molang.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import software.bluelib.api.molang.MoLangContext;
import software.bluelib.api.molang.MoLangRuntime;

public class GeneralMoLang implements MoLangContext {

    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, BiFunction<List<Object>, MoLangRuntime, Object>> functions = new HashMap<>();

    public GeneralMoLang() {
        variables.put("pi", Math.PI);
        registerFunction("say", (args, rt) -> {
            if (!args.isEmpty()) System.out.println(args.get(0));
            return null;
        });
    }

    public void registerFunction(String pName, BiFunction<List<Object>, MoLangRuntime, Object> pFunction) {
        functions.put(pName, pFunction);
    }

    public void setVariable(String pName, Object pValue) {
        variables.put(pName, pValue);
    }

    @Override
    public Object getVariable(String pName) {
        return variables.get(pName);
    }

    @Override
    public Object callFunction(String pName, List<Object> pArguments) {
        var fn = functions.get(pName);
        return fn != null ? fn.apply(pArguments, null) : null;
    }
}
