package software.bluelib.loader.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.loader.renderer.*;
import software.bluelib.loader.renderer.layer.BlueRenderLayer;


public interface BlueRenderEvent {
	
	BlueRenderer<?> getRenderer();

	
	abstract class Armor implements BlueRenderEvent {
		private final BlueArmorRenderer<?> renderer;

		public Armor(BlueArmorRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public BlueArmorRenderer<?> getRenderer() {
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
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Pre(BlueArmorRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				boolean handle(Pre event);
			}
		}

		
		public static class Post extends Armor {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Post(BlueArmorRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(Post event);
			}
		}

		
		public static class CompileRenderLayers extends Armor {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(BlueArmorRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(BlueRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Block implements BlueRenderEvent {
		private final BlueBlockRenderer<?> renderer;

		public Block(BlueBlockRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public BlueBlockRenderer<?> getRenderer() {
			return this.renderer;
		}

		
		public BlockEntity getBlockEntity() {
			return getRenderer().getAnimatable();
		}

		
		public static class Pre extends Block {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Pre(BlueBlockRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				boolean handle(Pre event);
			}
		}

		
		public static class Post extends Block {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Post(BlueBlockRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(Post event);
			}
		}

		
		public static class CompileRenderLayers extends Block {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(BlueBlockRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(BlueRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Entity implements BlueRenderEvent {
		private final BlueEntityRenderer<?> renderer;

		public Entity(BlueEntityRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public BlueEntityRenderer<?> getRenderer() {
			return this.renderer;
		}

		
		public net.minecraft.world.entity.Entity getEntity() {
			return this.renderer.getAnimatable();
		}

		
		public static class Pre extends Entity {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Pre(BlueEntityRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				boolean handle(Pre event);
			}
		}

		
		public static class Post extends Entity {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Post(BlueEntityRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(Post event);
			}
		}

		
		public static class CompileRenderLayers extends Entity {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(BlueEntityRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(BlueRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Item implements BlueRenderEvent {
		private final BlueItemRenderer<?> renderer;

		public Item(BlueItemRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public BlueItemRenderer<?> getRenderer() {
			return this.renderer;
		}

		
		public ItemStack getItemStack() {
			return getRenderer().getCurrentItemStack();
		}

		
		public static class Pre extends Item {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Pre(BlueItemRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				boolean handle(Pre event);
			}
		}

		
		public static class Post extends Item {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Post(BlueItemRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(Post event);
			}
		}

		
		public static class CompileRenderLayers extends Item {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(BlueItemRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(BlueRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Object implements BlueRenderEvent {
		private final BlueObjectRenderer<?> renderer;

		public Object(BlueObjectRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public BlueObjectRenderer<?> getRenderer() {
			return this.renderer;
		}

		
		public static class Pre extends Object {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Pre(BlueObjectRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				boolean handle(Pre event);
			}
		}

		
		public static class Post extends Object {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Post(BlueObjectRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(Post event);
			}
		}

		
		public static class CompileRenderLayers extends Object {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(BlueObjectRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(BlueRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class ReplacedEntity implements BlueRenderEvent {
		private final BlueReplacedEntityRenderer<?, ?> renderer;

		public ReplacedEntity(BlueReplacedEntityRenderer<?, ?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public BlueReplacedEntityRenderer<?, ?> getRenderer() {
			return this.renderer;
		}

		
		public net.minecraft.world.entity.Entity getReplacedEntity() {
			return getRenderer().getCurrentEntity();
		}

		
		public static class Pre extends ReplacedEntity {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, event -> true, listeners -> event -> {
				for (Listener listener : listeners) {
					if (!listener.handle(event))
						return false;
				}

				return true;
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Pre(BlueReplacedEntityRenderer<?, ?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				boolean handle(Pre event);
			}
		}

		
		public static class Post extends ReplacedEntity {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			private final PoseStack pPoseStack;
			private final ModelCache model;
			private final MultiBufferSource pBufferSource;
			private final float pPartialTick;
			private final int pPackedLight;

			public Post(BlueReplacedEntityRenderer<?, ?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
				super(renderer);

				this.pPoseStack = pPoseStack;
				this.model = model;
				this.pBufferSource = pBufferSource;
				this.pPartialTick = pPartialTick;
				this.pPackedLight = pPackedLight;
			}

			public PoseStack getPoseStack() {
				return this.pPoseStack;
			}

			public ModelCache getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.pBufferSource;
			}

			public float getPartialTick() {
				return this.pPartialTick;
			}

			public int getPackedLight() {
				return this.pPackedLight;
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(Post event);
			}
		}

		
		public static class CompileRenderLayers extends ReplacedEntity {
			public static final Event<Listener> EVENT = EventFactory.createArrayBacked(Listener.class, post -> {}, listeners -> event -> {
				for (Listener listener : listeners) {
					listener.handle(event);
				}
			});

			public CompileRenderLayers(BlueReplacedEntityRenderer<?, ?> renderer) {
				super(renderer);
			}

			
			public void addLayer(BlueRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}
}
