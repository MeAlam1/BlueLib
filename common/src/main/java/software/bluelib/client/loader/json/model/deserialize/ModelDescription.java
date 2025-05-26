/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.model.deserialize;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.JsonUtils;

public record ModelDescription(
        @Nullable Boolean animationArmsDown,
        @Nullable Boolean animationArmsOutFront,
        @Nullable Boolean animationDontShowArmor,
        @Nullable Boolean animationInvertedCrouch,
        @Nullable Boolean animationNoHeadBob,
        @Nullable Boolean animationSingleArmAnimation,
        @Nullable Boolean animationSingleLegAnimation,
        @Nullable Boolean animationStationaryLegs,
        @Nullable Boolean animationStatueOfLibertyArms,
        @Nullable Boolean animationUpsideDown,
        @Nullable String identifier,
        @Nullable Boolean preserveModelPose,
        float textureHeight,
        float textureWidth,
        @Nullable Float visibleBoundsHeight,
        List<Float> visibleBoundsOffset,
        @Nullable Float visibleBoundsWidth) {

    public static JsonDeserializer<ModelDescription> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            JsonObject obj = json.getAsJsonObject();
            Boolean animationArmsDown = JsonUtils.getOptionalBoolean(obj, "animationArmsDown");
            Boolean animationArmsOutFront = JsonUtils.getOptionalBoolean(obj, "animationArmsOutFront");
            Boolean animationDontShowArmor = JsonUtils.getOptionalBoolean(obj, "animationDontShowArmor");
            Boolean animationInvertedCrouch = JsonUtils.getOptionalBoolean(obj, "animationInvertedCrouch");
            Boolean animationNoHeadBob = JsonUtils.getOptionalBoolean(obj, "animationNoHeadBob");
            Boolean animationSingleArmAnimation = JsonUtils.getOptionalBoolean(obj, "animationSingleArmAnimation");
            Boolean animationSingleLegAnimation = JsonUtils.getOptionalBoolean(obj, "animationSingleLegAnimation");
            Boolean animationStationaryLegs = JsonUtils.getOptionalBoolean(obj, "animationStationaryLegs");
            Boolean animationStatueOfLibertyArms = JsonUtils.getOptionalBoolean(obj, "animationStatueOfLibertyArms");
            Boolean animationUpsideDown = JsonUtils.getOptionalBoolean(obj, "animationUpsideDown");
            String identifier = GsonHelper.getAsString(obj, "identifier", null);
            Boolean preserveModelPose = JsonUtils.getOptionalBoolean(obj, "preserve_model_pose");
            float textureHeight = GsonHelper.getAsFloat(obj, "texture_height");
            float textureWidth = GsonHelper.getAsFloat(obj, "texture_width");
            Float visibleBoundsHeight = JsonUtils.getOptionalFloat(obj, "visible_bounds_height");
            List<Float> visibleBoundsOffset = JsonUtils.jsonArrayToFloatList(GsonHelper.getAsJsonArray(obj, "visible_bounds_offset", null));
            Float visibleBoundsWidth = JsonUtils.getOptionalFloat(obj, "visible_bounds_width");

            return new ModelDescription(
                    animationArmsDown,
                    animationArmsOutFront,
                    animationDontShowArmor,
                    animationInvertedCrouch,
                    animationNoHeadBob,
                    animationSingleArmAnimation,
                    animationSingleLegAnimation,
                    animationStationaryLegs,
                    animationStatueOfLibertyArms,
                    animationUpsideDown,
                    identifier,
                    preserveModelPose,
                    textureHeight,
                    textureWidth,
                    visibleBoundsHeight,
                    visibleBoundsOffset,
                    visibleBoundsWidth);
        };
    }
}
