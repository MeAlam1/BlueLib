/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.loading.json;

import com.google.gson.annotations.SerializedName;

public enum FormatVersion {
    @SerializedName("1.12.0")
    V_1_12_0,
    @SerializedName("1.14.0")
    V_1_14_0,
    @SerializedName("1.21.0")
    V_1_21_0
}
