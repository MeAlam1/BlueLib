# Server to Client Packets

This package contains the **Packet Classes** for **Server to Client (S2C)** communication.

Each packet:

- Implements `NetworkPacket<T>`.
- Handles encoding/decoding data via `RegistryFriendlyByteBuf`.
- Has a unique `ResourceLocation ID`.
- Is linked to a handler in the `client.net` package.

---

## S2C Packet Structure

A typical S2C Packet contains:

- Fields representing the data.
- A static `ResourceLocation` ID.
- `encode` method to write data.
- Static `decode` method to read data.
- `getId` method returning the packet ID.

---

## Example

```java

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.net.NetworkPacket;

/**
 * {@link TestPacketHandler}
 */
public class TestPacket implements NetworkPacket<TestPacket> {
	public final boolean value;
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, "test_packet");

	public TestPacket(boolean value) {
		this.value = value;
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer) {
		buffer.writeBoolean(value);
	}

	public static TestPacket decode(RegistryFriendlyByteBuf buffer) {
		return new TestPacket(buffer.readBoolean());
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}
}
```

# Important
* Create a handler in `client.net`.
* Register the packet in your own `NetworkRegistry.getS2CPackets()` method.
```java
list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode));
```
* and `ClientNetworkRegistry.getS2CPackets()`
```java
list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, TestPacketHandler::new));
```
