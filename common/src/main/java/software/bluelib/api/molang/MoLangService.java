package software.bluelib.api.molang;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import software.bluelib.api.molang.context.BaseMoLangContext;
import software.bluelib.api.molang.context.GeneralMoLang;
import software.bluelib.api.molang.context.MathMoLang;
import software.bluelib.api.molang.context.OperatorMoLang;

public class MoLangService {

    private final Map<MoLangType, MoLangRuntime> runtimes = new HashMap<>();

    public MoLangService() {
        Map<MoLangType, Supplier<BaseMoLangContext>> contextFactories = Map.of(
                MoLangType.GENERAL, GeneralMoLang::new,
                MoLangType.MATH, MathMoLang::new,
                MoLangType.OPERATOR, OperatorMoLang::new);

        for (MoLangType type : MoLangType.values()) {
            MoLangRuntime runtime = new MoLangRuntime();
            register(type, runtime);

            Supplier<BaseMoLangContext> factory = contextFactories.get(type);
            if (factory != null) {
                runtime.registerContext(type.id(), factory.get());
            }
        }
    }

    public void register(MoLangType pType, MoLangRuntime pRuntime) {
        runtimes.put(pType, pRuntime);
    }

    public MoLangRuntime getRuntimeFor(MoLangType pType) {
        MoLangRuntime runtime = runtimes.get(pType);
        if (runtime == null) {
            throw new IllegalArgumentException("No MoLangRuntime registered for type: " + pType.id());
        }
        return runtime;
    }
}
