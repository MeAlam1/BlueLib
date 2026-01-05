package software.bluelib_examples.client.registry;

import software.bluelib.api.net.PacketProvider;
import software.bluelib.net.PacketRegisterInfo;

import java.util.ArrayList;
import java.util.List;

public class ClientNetworkRegistry implements PacketProvider {

    @Override
    public List<PacketRegisterInfo<?>> getC2SPackets() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        return list;
    }

    @Override
    public List<PacketRegisterInfo<?>> getS2CPackets() {
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        return list;
    }
}
