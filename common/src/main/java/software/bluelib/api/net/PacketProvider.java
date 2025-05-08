// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.net;

import java.util.List;
import software.bluelib.net.PacketRegisterInfo;

public interface PacketProvider {

    interface C2SPacketProvider {

        List<PacketRegisterInfo<?>> getC2SPacketInfoList();
    }

    interface S2CPacketProvider {

        List<PacketRegisterInfo<?>> getS2CPacketInfoList();
    }
}
