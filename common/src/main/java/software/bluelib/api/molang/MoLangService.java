package software.bluelib.api.molang;

import software.bluelib.api.molang.context.BaseMoLangContext;
import software.bluelib.api.molang.context.GeneralMoLang;
import software.bluelib.api.molang.context.MathMoLang;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MoLangService {

	private final Map<MoLangType, MoLangRuntime> runtimes = new HashMap<>();

	public MoLangService() {
		Map<MoLangType, Supplier<BaseMoLangContext>> contextFactories = Map.of(
				MoLangType.GENERAL, GeneralMoLang::new,
				MoLangType.MATH, MathMoLang::new
		);
		

		for (MoLangType type : contextFactories.keySet()) {
			MoLangRuntime runtime = new MoLangRuntime();
			register(type, runtime);
			runtime.registerContext(type.id().substring(0, 1).toLowerCase(), contextFactories.get(type).get());
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
