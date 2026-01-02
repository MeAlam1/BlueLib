package software.bluelib.internal.registry;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.PacketTypeProvider;
import software.bluelib.net.PacketRegisterInfo;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.client.loader.BlockEntityAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.BlockEntityDataSyncPacket;
import software.bluelib.net.messages.client.loader.EntityAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.EntityDataSyncPacket;
import software.bluelib.net.messages.client.loader.SingletonAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.SingletonDataSyncPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredBlockEntityAnimPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredEntityAnimPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredSingletonAnimPacket;
import software.bluelib.net.messages.server.TestPacket;

@ApiStatus.Internal
public class BluePacketTypes implements PacketTypeProvider {

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getC2SPackets() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();
		// Types only: handler supplier is not used for payload-type registration,
		// so provide a dummy handler supplier that never runs on the wrong side.
		list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, () -> null));
		return list;
	}

	@Override
	public @NotNull List<PacketRegisterInfo<?>> getS2CPackets() {
		List<PacketRegisterInfo<?>> list = new ArrayList<>();
		list.add(new PacketRegisterInfo<>(OpenLoggerPacket.ID, OpenLoggerPacket::decode, () -> null));

		list.add(new PacketRegisterInfo<>(BlockEntityAnimTriggerPacket.ID, BlockEntityAnimTriggerPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(BlockEntityDataSyncPacket.ID, BlockEntityDataSyncPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(EntityAnimTriggerPacket.ID, EntityAnimTriggerPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(EntityDataSyncPacket.ID, EntityDataSyncPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(SingletonAnimTriggerPacket.ID, SingletonAnimTriggerPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(SingletonDataSyncPacket.ID, SingletonDataSyncPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(StopTriggeredEntityAnimPacket.ID, StopTriggeredEntityAnimPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(StopTriggeredBlockEntityAnimPacket.ID, StopTriggeredBlockEntityAnimPacket::decode, () -> null));
		list.add(new PacketRegisterInfo<>(StopTriggeredSingletonAnimPacket.ID, StopTriggeredSingletonAnimPacket::decode, () -> null));

		return list;
	}
}