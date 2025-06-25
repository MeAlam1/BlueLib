/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json;

import java.util.Map;
import java.util.function.Function;

public interface CacheFactory<T, S> {

    T construct(S pSource);

    static <T, S, F extends CacheFactory<T, S>> T constructWithFactory(
            Function<String, F> pFactoryGetter, String pNamespace, S pSource) {
        return pFactoryGetter.apply(pNamespace).construct(pSource);
    }

    static <F> F getForNamespace(Map<String, F> pFactories, F pDefaultFactory, String pNamespace) {
        return pFactories.getOrDefault(pNamespace, pDefaultFactory);
    }

    static <F> void register(Map<String, F> pFactories, String pNamespace, F pFactory) {
        pFactories.put(pNamespace, pFactory);
    }
}
