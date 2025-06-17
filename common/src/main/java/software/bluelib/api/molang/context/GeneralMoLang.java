package software.bluelib.api.molang.context;

public class GeneralMoLang extends BaseMoLangContext {

    public GeneralMoLang() {
        registerFunction("say", (args, rt) -> {
            if (!args.isEmpty()) System.out.println(args.get(0));
            return null;
        });
    }
}
