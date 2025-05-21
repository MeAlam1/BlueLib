package software.bluelib_examples.registry;

import software.bluelib.api.net.PacketProvider;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.server.TestPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

import java.util.ArrayList;
import java.util.List;

public class NetworkRegistry implements PacketProvider.C2SPacketProvider, PacketProvider.S2CPacketProvider {

	@Override
	public List<PacketRegisterInfo<?>> getC2SPacketInfoList() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		// Register your Client-to-Server packets here
		list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler()));

		return list;
	}

	@Override
	public List<PacketRegisterInfo<?>> getS2CPacketInfoList() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();

		// Register your Server-to-Client packets here
		list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler()));

		return list;
	}
}
