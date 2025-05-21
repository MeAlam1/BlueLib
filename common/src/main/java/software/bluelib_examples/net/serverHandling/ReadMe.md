# Client to Server Packethandlers

This package contains **handlers** for processing **Client to Server (C2S)** packets.

Each handler:

- Implements `ServerNetworkPacketHandler<T>`.
- Defines how to process received packet data.
- Must match a packet from the `net.messages.server` package.

---

## C2S Packet Handler Structure

A typical handler:

- Implements `handle` method.
- Has access to the `Packet`, the `MinecraftServer`, and the `ServerPlayer` who received it.

---

## Example

```java

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.api.net.ServerNetworkPacketHandler;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class TestPacketHandler implements ServerNetworkPacketHandler<TestPacket> {

	@Override
	public void handle(TestPacket pPacket, MinecraftServer pServer, ServerPlayer pPlayer) {
		BaseLogger.log(BaseLogLevel.ERROR, Component.literal("Received TestPacket: " + pPacket.value()));
	}
}
```

# Important

* Create a packet in `net.messages.server`.
* Register the packet in your own `NetworkRegistry.getC2SPacketInfoList()` method.

```java
list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler()));
```