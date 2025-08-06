package software.bluelib.api.registry.helpers.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RenderHelper {

	private static final List<BiConsumer<BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>, BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>>> renderers = new ArrayList<>();

	public static void queueRenderer(BiConsumer<BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>, BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>> consumer) {
		renderers.add(consumer);
	}

	public static void registerRenderers(BiConsumer<EntityType<? extends Entity>, EntityRendererProvider> entityConsumer, BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider> blockConsumer) {
		for (var r : renderers) {
			r.accept(entityConsumer, blockConsumer);
		}
	}
}
