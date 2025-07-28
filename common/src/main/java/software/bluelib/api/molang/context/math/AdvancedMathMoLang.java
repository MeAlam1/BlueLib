package software.bluelib.api.molang.context.math;

import software.bluelib.api.molang.MoLangNamespaceUtils;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AdvancedMathMoLang extends BaseMoLangContext {

	public AdvancedMathMoLang() {
		setVariable(MoLangNamespaceUtils.withMathNamespace("pi"), Math.PI);
		setVariable(MoLangNamespaceUtils.withMathNamespace("e"), Math.E);
		registerFunction(MoLangNamespaceUtils.withMathNamespace("sqrt"), (args, runtime) -> Math.sqrt(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("pow"), (args, runtime) -> Math.pow(MoLangMathUtils.toDouble(args, 0), MoLangMathUtils.toDouble(args, 1)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("log"), (args, runtime) -> Math.log(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("exp"), (args, runtime) -> Math.exp(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("sign"), (args, runtime) -> Math.signum(MoLangMathUtils.toDouble(args, 0)));
	}
}