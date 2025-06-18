package software.bluelib.api.molang;

import java.util.HashMap;
import java.util.Map;

public class MoLang {

	public static final MoLangService service = new MoLangService();

	private static final Map<String, MoLangType> PREFIX_MAP = new HashMap<>();

	static {
		for (MoLangType type : MoLangType.values()) {
			PREFIX_MAP.put(type.id() + ".", type);
		}
	}

	public static void registerPrefix(String pPrefix, MoLangType pType) {
		PREFIX_MAP.put(pPrefix, pType);
	}

	public static Object autoMoLang(String pExpression) {
		if (pExpression == null) return null;
		for (Map.Entry<String, MoLangType> entry : PREFIX_MAP.entrySet()) {
			if (pExpression.startsWith(entry.getKey())) {
				String expr = pExpression.substring(entry.getKey().length());
				expr = entry.getValue().id() + "." + expr;
				return service.getRuntimeFor(entry.getValue()).evaluate(expr);
			}
		}
		return service.getRuntimeFor(MoLangType.GENERAL).evaluate(pExpression);
	}


}
