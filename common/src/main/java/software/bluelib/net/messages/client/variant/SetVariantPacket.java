package software.bluelib.net.messages.client.variant;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.internal.BlueResource;

public record SetVariantPacket(int entityId, @NotNull String variant) implements NetworkPacket<SetVariantPacket> {

	@NotNull
	public static final ResourceLocation ID = BlueResource.resource("set_variant_packet");

	@Override
	public void encode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		pBuffer.writeVarInt(entityId);
		pBuffer.writeUtf(variant);
	}

	public static @NotNull SetVariantPacket decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
		int entityId = pBuffer.readVarInt();
		String variant = pBuffer.readUtf();
		return new SetVariantPacket(entityId, variant);
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}
}