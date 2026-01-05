# Server to Client Packethandlers

This package contains **handlers** for processing **Server to Client (S2C)** packets.

Each handler:

- Implements `ClientNetworkPacketHandler<T>`.
- Defines how to process received packet data.
- Must match a packet from the `net.messages.client` package.

---

## S2C Packet Handler Structure

A typical handler:

- Implements `handle` method.
- Has access to the `Packet` and the `Minecraft` who received it.

---

## Example

```java

import net.minecraft.network.chat.Component;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.net.messages.client.TestPacket;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class TestPacketHandler implements ClientNetworkPacketHandler<TestPacket> {

	@Override
	public void handle(TestPacket pPacket, Minecraft pClient) {
		BaseLogger.log(BaseLogLevel.ERROR, Component.literal("Received TestPacket: " + pPacket.value));
	}
}
```

# Important

* Create a packet in `net.messages.client`.
* Register the packet in your own `NetworkRegistry.getS2CPackets()` method.
```java
list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode));
```
* and `ClientNetworkRegistry.getS2CPackets()`
```java
list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, TestPacketHandler::new));
```
