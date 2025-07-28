package software.bluelib.api.molang.context.math;

import software.bluelib.api.molang.MoLangNamespaceUtils;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class TrigMoLang extends BaseMoLangContext {

	public TrigMoLang() {
		registerFunction(MoLangNamespaceUtils.withMathNamespace("sin"), (args, runtime) -> Math.sin(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("cos"), (args, runtime) -> Math.cos(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("tan"), (args, runtime) -> Math.tan(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("asin"), (args, runtime) -> Math.asin(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("acos"), (args, runtime) -> Math.acos(MoLangMathUtils.toDouble(args, 0)));
		registerFunction(MoLangNamespaceUtils.withMathNamespace("atan"), (args, runtime) -> Math.atan(MoLangMathUtils.toDouble(args, 0)));
	}
}