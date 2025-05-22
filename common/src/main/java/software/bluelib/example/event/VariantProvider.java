/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.example.event;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.entity.variant.IVariantProvider;

@ApiStatus.Internal
public class VariantProvider implements IVariantProvider {

    @Override
    public List<String> getEntityNames() {
        return List.of("exampleone", "exampletwo");
    }
}
