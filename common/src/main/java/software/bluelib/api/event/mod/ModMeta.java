// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event.mod;

import java.util.Optional;

public record ModMeta(
        String modId,
        String displayName,
        String version,
        String description,
        Optional<String> logoFile

) {}
