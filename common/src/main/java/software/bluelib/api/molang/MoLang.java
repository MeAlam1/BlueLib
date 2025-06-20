/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import software.bluelib.api.molang.context.EntityMoLang;

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

    public static Object moLangWithContext(String pExpression, MoLangType pType, MoLangContext pContext) {
        if (pExpression == null) return null;

        MoLangRuntime runtime = service.getRuntimeFor(pType);
        String prefix = pType.id() + ".";

        runtime.pushContext(pType.id(), pContext);
        try {
            if (pExpression.startsWith(prefix)) {
                return runtime.evaluate(pExpression);
            }
            String transformed = prefix + pExpression;
            return runtime.evaluate(transformed);
        } finally {
            runtime.popContext(pType.id());
        }
    }

    public static Object moLangEntity(String pExpression, Entity pEntity) {
        return moLangWithContext(pExpression, MoLangType.ENTITY, new EntityMoLang(() -> pEntity));
    }
}
