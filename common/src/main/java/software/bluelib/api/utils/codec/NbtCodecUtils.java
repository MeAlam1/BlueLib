package software.bluelib.api.utils.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;

public final class NbtCodecUtils {

	private NbtCodecUtils() {
	}

	@NotNull
	public static <T> Codec<T> fromNbt(@NotNull Function<CompoundTag, T> pReader, @NotNull BiConsumer<T, CompoundTag> pWriter) {
		return Codec.PASSTHROUGH.comapFlatMap(
				dynamic -> {
					CompoundTag tag = (CompoundTag) dynamic.convert(NbtOps.INSTANCE).getValue();
					return DataResult.success(pReader.apply(tag));
				},
				value -> {
					CompoundTag tag = new CompoundTag();
					pWriter.accept(value, tag);
					return new Dynamic<>(NbtOps.INSTANCE, tag);
				});
	}

	@NotNull
	public static <T> DataComponentType<T> persistentDataType(@NotNull Codec<T> pCodec) {
		return DataComponentType.<T>builder()
				.persistent(pCodec)
				.build();
	}
}