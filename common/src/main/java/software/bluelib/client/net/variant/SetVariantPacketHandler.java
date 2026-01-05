package software.bluelib.client.net.variant;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.net.ClientNetworkPacketHandler;
import software.bluelib.entity.variant.IVariantAccessor;
import software.bluelib.net.messages.client.variant.SetVariantPacket;

public class SetVariantPacketHandler implements ClientNetworkPacketHandler<SetVariantPacket> {

	@Override
	public void handle(@NotNull SetVariantPacket pPacket, @NotNull Minecraft pClient) {
		pClient.execute(() -> {
			ClientLevel level = pClient.level;
			if (level == null) return;

			Entity e = level.getEntity(pPacket.entityId());
			if (!(e instanceof LivingEntity living)) return;

			((IVariantAccessor) living).setEntityVariantName(pPacket.variant());
		});
	}
}