/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public record FaceUVDeserializer(
		@Nullable String materialInstance,
		@NotNull List<Float> uv,
		@NotNull List<Float> uvSize,
		@NotNull Rotation uvRotation) {

	public FaceUVDeserializer(@Nullable String pMaterialInstance, @NotNull List<Float> pUv, @NotNull List<Float> pUvSize) {
		this(pMaterialInstance, pUv, pUvSize, Rotation.NONE);
	}

	@NotNull
	public static JsonDeserializer<FaceUVDeserializer> deserializer() throws JsonParseException {
		return (json, type, context) -> {
			JsonObject obj = json.getAsJsonObject();

			String materialInstance = JsonUtils.getOptionalString(obj, "material_instance");
			List<Float> uv = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "uv"));
			List<Float> uvSize = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "uv_size"));
			Rotation uvRotation = Rotation.fromValue(GsonHelper.getAsInt(obj, "uv_rotation", 0));

			return new FaceUVDeserializer(
					materialInstance,
					uv,
					uvSize,
					uvRotation);
		};
	}

	public enum Rotation {

		NONE,
		CLOCKWISE_90,
		CLOCKWISE_180,
		CLOCKWISE_270;

		@NotNull
		public static Rotation fromValue(@NotNull Integer pValue) {
			try {
				return Rotation.values()[(pValue % 360) / 90];
			} catch (Exception pException) {
				Rotation rotation = fromValue(Mth.floor(Math.abs(pValue) / 90f) * 90);
				BaseLogger.log(BaseLogLevel.ERROR, "Invalid rotation value: " + pValue + ", defaulting to" + rotation, pException);
				return rotation;
			}
		}

		@NotNull
		public List<Float> rotateUvs(@NotNull Float pU, @NotNull Float pV, @NotNull Float pUWidth, @NotNull Float pVHeight) {
			return switch (this) {
				case NONE -> List.of(pU, pV, pUWidth, pV, pUWidth, pVHeight, pU, pVHeight);
				case CLOCKWISE_90 -> List.of(pUWidth, pV, pUWidth, pVHeight, pU, pVHeight, pU, pV);
				case CLOCKWISE_180 -> List.of(pUWidth, pVHeight, pU, pVHeight, pU, pV, pUWidth, pV);
				case CLOCKWISE_270 -> List.of(pU, pVHeight, pU, pV, pUWidth, pV, pUWidth, pVHeight);
			};
		}
	}
}
