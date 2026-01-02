package software.bluelib.api.net;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import software.bluelib.net.PacketRegisterInfo;

public interface PacketTypeProvider {
	@NotNull List<PacketRegisterInfo<?>> getC2SPackets();

	@NotNull List<PacketRegisterInfo<?>> getS2CPackets();
}