package software.bluelib.api.molang;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface MoLangContext {

    @Nullable
    Object getVariable(String pName);

    @Nullable
    Object callFunction(String pName, List<Object> pArguments);
}
