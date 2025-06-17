package software.bluelib.api.molang;

import java.util.HashMap;
import java.util.Map;
import software.bluelib.api.molang.context.GeneralMoLang;

public class MoLangService {

    private final Map<MoLangType, MoLangRuntime> runtimes = new HashMap<>();

    public MoLangService() {
        register(MoLangType.GENERAL, new MoLangRuntime());
        setupRuntimes();
    }

    private void setupRuntimes() {
        MoLangRuntime general = getRuntimeFor(MoLangType.GENERAL);
        general.registerContext("g", new GeneralMoLang());
    }

    public void register(MoLangType pType, MoLangRuntime pRuntime) {
        runtimes.put(pType, pRuntime);
    }

    public MoLangRuntime getRuntimeFor(MoLangType pType) {
        MoLangRuntime runtime = runtimes.get(pType);
        if (runtime == null) {
            throw new IllegalArgumentException("No MoLangRuntime registered for type: " + pType.pId());
        }
        return runtime;
    }
}
