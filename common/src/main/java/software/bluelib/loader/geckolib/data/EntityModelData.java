/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.data;

import software.bluelib.api.annotations.WillBeDeprecated;

@WillBeDeprecated(since = "2.5.0", reason = "EntityModelData will be made redundant with the new MoLang System.")
public record EntityModelData(boolean isSitting, boolean isChild, float netHeadYaw, float headPitch) {}
