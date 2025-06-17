package software.bluelib.api.molang;

public class MoLangConstants {

	public static final MoLangService service = new MoLangService();

	public static Object generalMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.GENERAL).evaluate(pExpression);
	}

	public static Object mathMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.MATH).evaluate(pExpression);
	}

	public static Object livingEntityMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.LIVING_ENTITY).evaluate(pExpression);
	}


}
