/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.bluelib.client.loader.cache.animations.AnimationsCache;
import software.bluelib.client.loader.json.model.deserialize.*;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.loading.json.typeadapter.BakedAnimationsAdapter;
import software.bluelib.loader.loading.json.typeadapter.KeyFramesAdapter;

public class ModelLoader {

    public static final Gson MODEL_GSON = new GsonBuilder().setLenient()
            .registerTypeAdapter(Bone.class, Bone.deserializer())
            .registerTypeAdapter(Cube.class, Cube.deserializer())
            .registerTypeAdapter(FaceUV.class, FaceUV.deserializer())
            .registerTypeAdapter(LocatorClass.class, LocatorClass.deserializer())
            .registerTypeAdapter(LocatorValue.class, LocatorValue.deserializer())
            .registerTypeAdapter(ModelGeometry.class, ModelGeometry.deserializer())
            .registerTypeAdapter(Model.class, Model.deserializer())
            .registerTypeAdapter(ModelDescription.class, ModelDescription.deserializer())
            .registerTypeAdapter(PolyMesh.class, PolyMesh.deserializer())
            .registerTypeAdapter(PolysUnion.class, PolysUnion.deserializer())
            .registerTypeAdapter(TextureMesh.class, TextureMesh.deserializer())
            .registerTypeAdapter(UVFaces.class, UVFaces.deserializer())
            .registerTypeAdapter(UVUnion.class, UVUnion.deserializer())
            .registerTypeAdapter(Animation.Keyframes.class, new KeyFramesAdapter())
            .registerTypeAdapter(AnimationsCache.class, new BakedAnimationsAdapter())
            .create();
}
