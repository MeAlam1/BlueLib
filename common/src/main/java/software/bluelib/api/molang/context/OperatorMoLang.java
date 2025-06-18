package software.bluelib.api.molang.context;


/* TODO: Add
 * Arithmetic Operators
 * Comparison Operators
 * Logical Operators
 * Bitwise Operators
 * Assignment Operators
 * Unary Operators
 * SEPARATE METHODS FOR EACH OPERATOR
 */
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