package software.bluelib.api.molang;

public class MoLang {

	public static final MoLangService service = new MoLangService();

	public static Object generalMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.GENERAL).evaluate(pExpression);
	}

	public static Object mathMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.MATH).evaluate(pExpression);
	}
	
	public static Object operatorMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.OPERATOR).evaluate(pExpression);
	}

	public static Object entityMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.ENTITY).evaluate(pExpression);
	}

	public static Object animatableMoLang(String pExpression) {
		return service.getRuntimeFor(MoLangType.ANIMATABLE).evaluate(pExpression);
	}


}
