package software.bluelib.api.molang.context;

public class OperatorMoLang extends BaseMoLangContext {

	public OperatorMoLang() {
		registerFunction("add", (arguments, runtime) -> {
			double sum = 0;
			for (Object arg : arguments) {
				if (arg instanceof Number n) {
					sum += n.doubleValue();
				} else if (arg != null) {
					try {
						sum += Double.parseDouble(arg.toString());
					} catch (NumberFormatException ignored) {
					}
				}
			}
			return sum;
		});
	}
}