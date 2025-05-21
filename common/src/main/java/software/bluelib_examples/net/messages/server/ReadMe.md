# Client to Server Packets

This package contains the **Packet Classes** for **Client to Server (C2S)** communication.

Each packet:

- Implements `NetworkPacket<T>`.
- Handles encoding/decoding data via `RegistryFriendlyByteBuf`.
- Has a unique `ResourceLocation ID`.
- Is linked to a handler in the `net.serverhandling` package.

---

## C2S Packet Structure

A typical C2S Packet contains:

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
public record TestPacket(boolean value) implements NetworkPacket<TestPacket> {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, "test_packet");

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

* Create a handler in `net.serverHandling`.
* Register the packet in your own `NetworkRegistry.getC2SPacketInfoList()` method.

```java
list.add(new PacketRegisterInfo<>(TestPacket.ID, TestPacket::decode, new TestPacketHandler()));
```