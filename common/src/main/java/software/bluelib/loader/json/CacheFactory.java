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
import org.jetbrains.annotations.NotNull;

public interface CacheFactory<T, S> {

    @NotNull
    T construct(@NotNull S pSource);

    @NotNull
    static <T, S, F extends CacheFactory<T, S>> T constructWithFactory(
            @NotNull Function<String, F> pFactoryGetter,
            @NotNull String pNamespace,
            @NotNull S pSource) {
        return pFactoryGetter.apply(pNamespace).construct(pSource);
    }

    interface Registry<T, S, F extends CacheFactory<T, S>> {

        @NotNull
        Map<String, F> factories();

        @NotNull
        F defaultFactory();

        @NotNull
        default F getForNamespace(@NotNull String pNamespace) {
            return factories().getOrDefault(pNamespace, defaultFactory());
        }

        default void register(@NotNull String pNamespace, @NotNull F pFactory) {
            factories().put(pNamespace, pFactory);
        }
    }
}
