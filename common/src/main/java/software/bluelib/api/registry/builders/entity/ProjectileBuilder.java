/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry.builders.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ProjectileBuilder<T extends Entity> extends EntityBuilder<T, ProjectileBuilder<T>> {

	private final Class<T> entityClass;

	public ProjectileBuilder(String pName, EntityType.EntityFactory<T> pFactory, MobCategory pCategory, Class<T> pEntityClass, String pModId) {
		super(pName, pFactory, pCategory, pModId);
		this.entityClass = pEntityClass;
	}

	public ProjectileBuilder<T> sized(float pWidth, float pHeight) {
		this.width = pWidth;
		this.height = pHeight;
		return this;
	}

	public ProjectileBuilder<T> renderer(EntityRendererProvider<T> pRendererProvider) {
		this.rendererProvider = pRendererProvider;
		return this;
	}

	@Override
	protected ProjectileBuilder<T> self() {
		return this;
	}
}
