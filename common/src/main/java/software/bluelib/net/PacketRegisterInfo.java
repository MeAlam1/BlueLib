/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.net.Encodable;
import software.bluelib.api.net.NetworkPacket;
import software.bluelib.api.net.PacketHandler;

@SuppressWarnings({ "unused" })
public class PacketRegisterInfo<T extends NetworkPacket<T> & Encodable> {

	@NotNull
	private final ResourceLocation id;
	@NotNull
	private final Function<RegistryFriendlyByteBuf, T> decoder;
	@Nullable
	private final Supplier<PacketHandler<T>> handlerSupplier;
	@NotNull
	private final CustomPacketPayload.Type<T> payloadId;
	@NotNull
	private final StreamCodec<RegistryFriendlyByteBuf, T> codec;

	public PacketRegisterInfo(
			@NotNull ResourceLocation pId,
			@NotNull Function<RegistryFriendlyByteBuf, T> pDecoder,
			@Nullable Supplier<PacketHandler<T>> pHandlerSupplier,
			@Nullable StreamCodec<RegistryFriendlyByteBuf, T> pCodec) {
		this.id = pId;
		this.decoder = pDecoder;
		this.handlerSupplier = pHandlerSupplier;
		this.payloadId = new CustomPacketPayload.Type<>(pId);
		this.codec = pCodec != null ? pCodec : createDefaultCodec(pDecoder);
	}

	public PacketRegisterInfo(
			@NotNull ResourceLocation pId,
			@NotNull Function<RegistryFriendlyByteBuf, T> pDecoder,
			@Nullable Supplier<PacketHandler<T>> pHandlerSupplier) {
		this(pId, pDecoder, pHandlerSupplier, null);
	}

	public PacketRegisterInfo(
			@NotNull ResourceLocation pId,
			@NotNull Function<RegistryFriendlyByteBuf, T> pDecoder) {
		this(pId, pDecoder, null, null);
	}

	public PacketRegisterInfo(
			@NotNull ResourceLocation pId,
			@NotNull Function<RegistryFriendlyByteBuf, T> pDecoder,
			@Nullable StreamCodec<RegistryFriendlyByteBuf, T> pCodec) {
		this.id = pId;
		this.decoder = pDecoder;
		this.handlerSupplier = null;
		this.payloadId = new CustomPacketPayload.Type<>(pId);
		this.codec = pCodec != null ? pCodec : createDefaultCodec(pDecoder);
	}

	@NotNull
	private StreamCodec<RegistryFriendlyByteBuf, T> createDefaultCodec(@NotNull Function<RegistryFriendlyByteBuf, T> pDecoder) {
		return StreamCodec.of(
				(buf, packet) -> packet.encode(buf),
				pDecoder::apply);
	}

	@NotNull
	public ResourceLocation getId() {
		return id;
	}

	@NotNull
	public Function<RegistryFriendlyByteBuf, T> getDecoder() {
		return decoder;
	}

	@Nullable
	public PacketHandler<T> getHandler() {
		if (handlerSupplier == null) {
			return null;
		}
		return handlerSupplier.get();
	}

	@NotNull
	public CustomPacketPayload.Type<T> getPayloadId() {
		return payloadId;
	}

	@NotNull
	public StreamCodec<RegistryFriendlyByteBuf, T> getCodec() {
		return codec;
	}
}
