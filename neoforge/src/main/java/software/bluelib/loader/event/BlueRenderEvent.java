/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.oldLoader.renderer.*;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayer;

@SuppressWarnings("unused")
public interface BlueRenderEvent {

	@NotNull
	BlueRenderer<?> getRenderer();

	abstract class Armor extends Event implements BlueRenderEvent {

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

		public static class Pre extends Armor implements ICancellableEvent {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Pre(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class Post extends Armor {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Post(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class CompileRenderLayers extends Armor {

			public CompileRenderLayers(@NotNull BlueArmorRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}
		}
	}

	abstract class Block extends Event implements BlueRenderEvent {

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

		public static class Pre extends Block implements ICancellableEvent {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Pre(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class Post extends Block {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Post(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class CompileRenderLayers extends Block {

			public CompileRenderLayers(@NotNull BlueBlockRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}
		}
	}

	abstract class Entity extends Event implements BlueRenderEvent {

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

		public static class Pre extends Entity implements ICancellableEvent {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Pre(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class Post extends Entity {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Post(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class CompileRenderLayers extends Entity {

			public CompileRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}
		}
	}

	abstract class Item extends Event implements BlueRenderEvent {

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

		public static class Pre extends Item implements ICancellableEvent {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Pre(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class Post extends Item {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Post(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class CompileRenderLayers extends Item {

			public CompileRenderLayers(@NotNull BlueItemRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}
		}
	}

	abstract class Object extends Event implements BlueRenderEvent {

		@NotNull
		private final BlueObjectRenderer<?> renderer;

		public Object(@NotNull BlueObjectRenderer<?> pRenderer) {
			this.renderer = pRenderer;
		}

		@Override
		public @NotNull BlueObjectRenderer<?> getRenderer() {
			return this.renderer;
		}

		public static class Pre extends Object implements ICancellableEvent {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Pre(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class Post extends Object {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Post(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class CompileRenderLayers extends Object {

			public CompileRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}
		}
	}

	abstract class ReplacedEntity extends Event implements BlueRenderEvent {

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

		public static class Pre extends ReplacedEntity implements ICancellableEvent {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Pre(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class Post extends ReplacedEntity {

			@NotNull
			private final PoseStack pPoseStack;
			@NotNull
			private final ModelCache model;
			@NotNull
			private final MultiBufferSource pBufferSource;
			@NotNull
			private final Float pPartialTick;
			@NotNull
			private final Integer pPackedLight;

			public Post(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight) {
				super(pRenderer);

				this.pPoseStack = pPoseStack;
				this.model = pModel;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public @NotNull PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public @NotNull ModelCache getModel() {
				return this.model;
			}

			public @NotNull MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public @NotNull Float getPartialTick() {
				return this.pPartialTick;
			}

			public @NotNull Integer getPackedLight() {
				return this.pPackedLight;
			}
		}

		public static class CompileRenderLayers extends ReplacedEntity {

			public CompileRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer) {
				super(pRenderer);
			}

			public void addLayer(@NotNull BlueRenderLayer pRenderLayer) {
				getRenderer().addRenderLayer(pRenderLayer);
			}
		}
	}
}
