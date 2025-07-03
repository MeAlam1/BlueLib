/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */

package software.bluelib.example.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.example.entity.ExampleEntity;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.oldLoader.renderer.BlueRenderer;

public class ExampleModel extends BlueModel<ExampleEntity> {

	private final ResourceLocation model = ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "models/test.geo.json");
	private final ResourceLocation animations = ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "animations/test.animation.json");

	@Override
	public ResourceLocation getModelResource(ExampleEntity pExampleEntity, @Nullable BlueRenderer<ExampleEntity> pBlueRenderer) {
		return model;
	}

	@Override
	public ResourceLocation getModelResource(ExampleEntity pAnimatable) {
		return model;
	}

	@Override
	public ResourceLocation getTextureResource(ExampleEntity pExampleEntity, @Nullable BlueRenderer<ExampleEntity> pBlueRenderer) {
		return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "textures/test.png");
	}

	@Override
	public ResourceLocation getTextureResource(ExampleEntity pAnimatable) {
		return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, "textures/test.png");
	}

	@Override
	public ResourceLocation getAnimationResource(ExampleEntity pExampleEntity) {
		return animations;
	}
}
