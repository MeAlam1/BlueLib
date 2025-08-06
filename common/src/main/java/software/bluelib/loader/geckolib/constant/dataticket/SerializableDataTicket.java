/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.constant.dataticket;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.loader.geckolib.constant.DataTickets;

@WillBeDeprecated(since = "2.5.0", reason = "DataTicket will be made redundant with the new MoLang System.")
public abstract class SerializableDataTicket<D> extends DataTicket<D> {

	public static final StreamCodec<RegistryFriendlyByteBuf, SerializableDataTicket<?>> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			SerializableDataTicket::id,
			DataTickets::byName);

	public SerializableDataTicket(String id, Class<? extends D> objectType) {
		super(id, objectType);
	}

	public abstract StreamCodec<? super RegistryFriendlyByteBuf, D> streamCodec();

	// Pre-defined typings for use

	public static SerializableDataTicket<Double> ofDouble(ResourceLocation id) {
		return new SerializableDataTicket<>(id.toString(), Double.class) {

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, Double> streamCodec() {
				return ByteBufCodecs.DOUBLE;
			}
		};
	}

	public static SerializableDataTicket<Float> ofFloat(ResourceLocation id) {
		return new SerializableDataTicket<>(id.toString(), Float.class) {

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, Float> streamCodec() {
				return ByteBufCodecs.FLOAT;
			}
		};
	}

	public static SerializableDataTicket<Boolean> ofBoolean(ResourceLocation id) {
		return new SerializableDataTicket<>(id.toString(), Boolean.class) {

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, Boolean> streamCodec() {
				return ByteBufCodecs.BOOL;
			}
		};
	}

	public static SerializableDataTicket<Integer> ofInt(ResourceLocation id) {
		return new SerializableDataTicket<>(id.toString(), Integer.class) {

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, Integer> streamCodec() {
				return ByteBufCodecs.VAR_INT;
			}
		};
	}

	public static SerializableDataTicket<String> ofString(ResourceLocation id) {
		return new SerializableDataTicket<>(id.toString(), String.class) {

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, String> streamCodec() {
				return ByteBufCodecs.STRING_UTF8;
			}
		};
	}

	public static <E extends Enum<E>> SerializableDataTicket<E> ofEnum(ResourceLocation id, Class<E> enumClass) {
		return new SerializableDataTicket<>(id.toString(), enumClass) {

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, E> streamCodec() {
				return new StreamCodec<>() {

					@Override
					public E decode(RegistryFriendlyByteBuf buf) {
						return Enum.valueOf(enumClass, buf.readUtf());
					}

					@Override
					public void encode(RegistryFriendlyByteBuf buf, E data) {
						buf.writeUtf(data.toString());
					}
				};
			}
		};
	}
}
