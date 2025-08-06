package software.bluelib.api.registry.builders.entity;

import java.util.function.Supplier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.helpers.entity.RenderHelper;

public class ProjectileBuilder<T extends Entity> {

	private final String name;
	private final EntityType.EntityFactory<T> factory;
	private final MobCategory category;
	private final Class<T> entityClass;
	private float width;
	private float height;
	private EntityRendererProvider<T> rendererProvider = null;

	public ProjectileBuilder(String name, EntityType.EntityFactory<T> factory, MobCategory category, Class<T> entityClass) {
		this.name = name;
		this.factory = factory;
		this.category = category;
		this.entityClass = entityClass;
	}

	public ProjectileBuilder<T> sized(float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public ProjectileBuilder<T> renderer(EntityRendererProvider<T> rendererProvider) {
		this.rendererProvider = rendererProvider;
		return this;
	}

	public Supplier<EntityType<T>> register() {
		Supplier<EntityType<T>> entityTypeSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerEntity(name,
				() -> EntityType.Builder.of(factory, category)
						.sized(width, height)
						.build(name));

		if (rendererProvider != null) {
			RenderHelper.queueRenderer((entityConsumer, blockConsumer) -> {
				entityConsumer.accept(entityTypeSupplier.get(), rendererProvider);
			});
		}

		return entityTypeSupplier;
	}
}
