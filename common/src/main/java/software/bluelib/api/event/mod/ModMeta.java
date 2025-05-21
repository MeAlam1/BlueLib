/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.mod;

import java.util.Optional;

public record ModMeta(
        String modId,
        String displayName,
        String version,
        String description,
        Optional<String> logoFile

) {}
