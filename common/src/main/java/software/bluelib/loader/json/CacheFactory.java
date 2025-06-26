/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json;

import java.util.Map;
import java.util.function.Function;

public interface CacheFactory<T, S> {

    T construct(S pSource);

    static <T, S, F extends CacheFactory<T, S>> T constructWithFactory(
            Function<String, F> pFactoryGetter, String pNamespace, S pSource) {
        return pFactoryGetter.apply(pNamespace).construct(pSource);
    }

    interface Registry<T, S, F extends CacheFactory<T, S>> {

        Map<String, F> factories();

        F defaultFactory();

        default F getForNamespace(String pNamespace) {
            return factories().getOrDefault(pNamespace, defaultFactory());
        }

        default void register(String pNamespace, F pFactory) {
            factories().put(pNamespace, pFactory);
        }
    }
}
