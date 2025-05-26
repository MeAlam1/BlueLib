package software.bluelib_examples.registry;

import java.util.ArrayList;
import java.util.List;
import software.bluelib.api.net.PacketProvider;
import software.bluelib.net.PacketRegisterInfo;

public class NetworkRegistry implements PacketProvider.C2SPacketProvider, PacketProvider.S2CPacketProvider {

    @Override
    public List<PacketRegisterInfo<?>> getC2SPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        return list;
    }

    @Override
    public List<PacketRegisterInfo<?>> getS2CPacketInfoList() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        return list;
    }
}
