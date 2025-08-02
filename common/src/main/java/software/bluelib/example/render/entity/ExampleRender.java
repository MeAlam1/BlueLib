/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */

package software.bluelib.example.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bluelib.example.entity.ExampleEntity;
import software.bluelib.example.model.entity.ExampleModel;
import software.bluelib.loader.renderer.entity.BlueEntityRenderer;

public class ExampleRender extends BlueEntityRenderer<ExampleEntity> {

	public ExampleRender(EntityRendererProvider.Context pContext) {
		super(pContext, new ExampleModel());
	}
}
