package software.bluelib.api.molang.context;

public class MathMoLang extends BaseMoLangContext {

    public MathMoLang() {
        variables.put("pi", Math.PI);
        variables.put("e", Math.E);
    }
}
