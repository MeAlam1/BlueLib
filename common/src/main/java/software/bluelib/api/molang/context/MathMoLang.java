package software.bluelib.api.molang.context;

public class MathMoLang extends BaseMoLangContext {

    public MathMoLang() {
        setVariable("pi", Math.PI);
        setVariable("e", Math.E);
    }
}
