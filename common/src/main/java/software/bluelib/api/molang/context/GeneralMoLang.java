package software.bluelib.api.molang.context;

public class GeneralMoLang extends BaseMoLangContext {

    public GeneralMoLang() {
        registerFunction("print", (args, rt) -> {
            if (!args.isEmpty()) System.out.println(args.getFirst());
            return null;
        });
    }
}
