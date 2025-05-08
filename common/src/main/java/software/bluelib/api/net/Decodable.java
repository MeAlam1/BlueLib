// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.net;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface Decodable {

    void decode(RegistryFriendlyByteBuf pBuffer);
}
