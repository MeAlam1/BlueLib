/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.oldLoader.renderer.*;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayer;

@SuppressWarnings("unused")
public interface BlueRenderEvent {

	@NotNull
	BlueRenderer<?> getRenderer();

	abstract class Armor implements BlueRenderEvent {

		@NotNull
		private final BlueArmorRenderer<?> renderer;

		public Armor(@NotNull BlueArmorRenderer<?> pRenderer) {
			this.renderer = pRenderer;
		}

		@Override
		public @NotNull BlueArmorRenderer<?> getRenderer() {
			return this.renderer;
		}

		@Nullable
		public net.minecraft.world.entity.Entity getEntity() {
			return getRenderer().getCurrentEntity();
		}

		@Nullable
		public ItemStack getItemStack() {
			return getRenderer().getCurrentStack();
		}

		@Nullable
		public EquipmentSlot getEquipmentSlot() {
			return getRenderer().getCurrentSlot();
		}

		public static class Pre extends Armor {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Pre(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				boolean handle(@NotNull Pre pEvent);
			}
		}

		public static class Post extends Armor {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Post(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull Post pEvent);
			}
		}

		public static class CompileRenderLayers extends Armor {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(@NotNull BlueArmorRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull CompileRenderLayers pEvent);
			}
		}
	}

	abstract class Block implements BlueRenderEvent {

		@NotNull
		private final BlueBlockRenderer<?> renderer;

		public Block(@NotNull BlueBlockRenderer<?> pRenderer) {
			this.renderer = pRenderer;
		}

		@Override
		public @NotNull BlueBlockRenderer<?> getRenderer() {
			return this.renderer;
		}

		public @NotNull BlockEntity getBlockEntity() {
			return getRenderer().getAnimatable();
		}

		public static class Pre extends Block {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Pre(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				boolean handle(@NotNull Pre pEvent);
			}
		}

		public static class Post extends Block {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Post(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull Post pEvent);
			}
		}

		public static class CompileRenderLayers extends Block {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(@NotNull BlueBlockRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull CompileRenderLayers pEvent);
			}
		}
	}

	abstract class Entity implements BlueRenderEvent {

		@NotNull
		private final BlueEntityRenderer<?> renderer;

		public Entity(@NotNull BlueEntityRenderer<?> pRenderer) {
			this.renderer = pRenderer;
		}

		@Override
		public @NotNull BlueEntityRenderer<?> getRenderer() {
			return this.renderer;
		}

		public @NotNull net.minecraft.world.entity.Entity getEntity() {
			return this.renderer.getAnimatable();
		}

		public static class Pre extends Entity {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Pre(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				boolean handle(@NotNull Pre pEvent);
			}
		}

		public static class Post extends Entity {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Post(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull Post pEvent);
			}
		}

		public static class CompileRenderLayers extends Entity {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull CompileRenderLayers pEvent);
			}
		}
	}

	abstract class Item implements BlueRenderEvent {

		@NotNull
		private final BlueItemRenderer<?> renderer;

		public Item(@NotNull BlueItemRenderer<?> pRenderer) {
			this.renderer = pRenderer;
		}

		@Override
		public @NotNull BlueItemRenderer<?> getRenderer() {
			return this.renderer;
		}

		public @NotNull ItemStack getItemStack() {
			return getRenderer().getCurrentItemStack();
		}

		public static class Pre extends Item {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Pre(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				boolean handle(@NotNull Pre pEvent);
			}
		}

		public static class Post extends Item {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Post(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull Post pEvent);
			}
		}

		public static class CompileRenderLayers extends Item {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(@NotNull BlueItemRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull CompileRenderLayers pEvent);
			}
		}
	}

	abstract class Object implements BlueRenderEvent {

		@NotNull
		private final BlueObjectRenderer<?> renderer;

		public Object(@NotNull BlueObjectRenderer<?> pRenderer) {
			this.renderer = pRenderer;
		}

		@Override
		public @NotNull BlueObjectRenderer<?> getRenderer() {
			return this.renderer;
		}

		public static class Pre extends Object {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Pre(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				boolean handle(@NotNull Pre pEvent);
			}
		}

		public static class Post extends Object {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Post(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull Post pEvent);
			}
		}

		public static class CompileRenderLayers extends Object {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull CompileRenderLayers pEvent);
			}
		}
	}

	abstract class ReplacedEntity implements BlueRenderEvent {

		@NotNull
		private final BlueReplacedEntityRenderer<?, ?> renderer;

		public ReplacedEntity(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer) {
			this.renderer = pRenderer;
		}

		@Override
		public @NotNull BlueReplacedEntityRenderer<?, ?> getRenderer() {
			return this.renderer;
		}

		public @NotNull net.minecraft.world.entity.Entity getReplacedEntity() {
			return getRenderer().getCurrentEntity();
		}

		public static class Pre extends ReplacedEntity {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Pre(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				boolean handle(@NotNull Pre pEvent);
			}
		}

		public static class Post extends ReplacedEntity {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			@NotNull
			private final PoseStack poseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource bufferSource;
			@NotNull
			private final Float partialTick;
			@NotNull
			private final Integer packedLight;

			public Post(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.poseStack = pPoseStack;
				this.model = pModel;
				this.bufferSource = pBufferSource;
				this.partialTick = pPartialTick;
				this.packedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.poseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.partialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.packedLight;
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull Post pEvent);
			}
		}

		public static class CompileRenderLayers extends ReplacedEntity {

			@NotNull
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}

			@FunctionalInterface
			public interface Listener {

				void handle(@NotNull CompileRenderLayers pEvent);
			}
		}
	}
}
