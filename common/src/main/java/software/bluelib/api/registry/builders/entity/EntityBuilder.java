/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry.builders.entity;

import java.util.function.Supplier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.helpers.entity.RenderHelper;

public abstract class EntityBuilder<T extends Entity, SELF extends EntityBuilder<T, SELF>> {

	protected final String modId;
	protected final String name;
	protected final EntityType.EntityFactory<T> factory;
	protected final MobCategory category;
	protected float width;
	protected float height;
	protected EntityRendererProvider<T> rendererProvider = null;

	public EntityBuilder(String pName, EntityType.EntityFactory<T> pFactory, MobCategory pCategory, String pModId) {
		this.modId = pModId;
		this.name = pName;
		this.factory = pFactory;
		this.category = pCategory;
	}

	public SELF sized(float pWidth, float pHeight) {
		this.width = pWidth;
		this.height = pHeight;
		return self();
	}

	public SELF renderer(EntityRendererProvider<T> pRendererProvider) {
		this.rendererProvider = pRendererProvider;
		return self();
	}

	public Supplier<EntityType<T>> register() {
		Supplier<EntityType<T>> entityTypeSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerEntity(name,
				() -> EntityType.Builder.of(factory, category)
						.sized(width, height)
						.build(name));

		if (rendererProvider != null) {
            new RenderHelper().queueRenderer((entityConsumer, blockConsumer) -> {
				entityConsumer.accept(entityTypeSupplier.get(), rendererProvider);
			});
		}

		return entityTypeSupplier;
	}

	protected abstract SELF self();
}
