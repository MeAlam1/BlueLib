/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.entity.variant;

import java.util.List;

public interface IVariantProvider {

    List<String> getEntityNames();

    default String getBasePath() {
        return "variant/entity/";
    }
}
