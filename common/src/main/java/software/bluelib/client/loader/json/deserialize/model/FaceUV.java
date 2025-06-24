/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.deserialize.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record FaceUV(
        @Nullable String materialInstance,
        List<Float> uv,
        List<Float> uvSize,
        Rotation uvRotation) {

    public FaceUV(@Nullable String pMaterialInstance, List<Float> pUv, List<Float> pUvSize) {
        this(pMaterialInstance, pUv, pUvSize, Rotation.NONE);
    }

    public static JsonDeserializer<FaceUV> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            JsonObject obj = json.getAsJsonObject();

            String materialInstance = JsonUtils.getOptionalString(obj, "material_instance");
            List<Float> uv = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "uv"));
            List<Float> uvSize = JsonUtils.jsonArrayToFloatList(JsonUtils.getOptionalJsonArray(obj, "uv_size"));
            Rotation uvRotation = Rotation.fromValue(GsonHelper.getAsInt(obj, "uv_rotation", 0));

            return new FaceUV(
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

        public static Rotation fromValue(int pValue) {
            try {
                return Rotation.values()[(pValue % 360) / 90];
            } catch (Exception pException) {
                // TODO: Log a warning about an invalid rotation value
                return fromValue(Mth.floor(Math.abs(pValue) / 90f) * 90);
            }
        }

        public List<Float> rotateUvs(float pU, float pV, float pUWidth, float pVHeight) {
            return switch (this) {
                case NONE -> List.of(pU, pV, pUWidth, pV, pUWidth, pVHeight, pU, pVHeight);
                case CLOCKWISE_90 -> List.of(pUWidth, pV, pUWidth, pVHeight, pU, pVHeight, pU, pV);
                case CLOCKWISE_180 -> List.of(pUWidth, pVHeight, pU, pVHeight, pU, pV, pUWidth, pV);
                case CLOCKWISE_270 -> List.of(pU, pVHeight, pU, pV, pUWidth, pV, pUWidth, pVHeight);
            };
        }
    }
}
