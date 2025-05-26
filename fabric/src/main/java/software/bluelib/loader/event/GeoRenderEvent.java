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
import software.bluelib.loader.renderer.layer.GeoRenderLayer;


public interface GeoRenderEvent {
	
	GeoRenderer<?> getRenderer();

	
	abstract class Armor implements GeoRenderEvent {
		private final GeoArmorRenderer<?> renderer;

		public Armor(GeoArmorRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public GeoArmorRenderer<?> getRenderer() {
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

			public Pre(GeoArmorRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public Post(GeoArmorRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public CompileRenderLayers(GeoArmorRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Block implements GeoRenderEvent {
		private final GeoBlockRenderer<?> renderer;

		public Block(GeoBlockRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public GeoBlockRenderer<?> getRenderer() {
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

			public Pre(GeoBlockRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public Post(GeoBlockRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public CompileRenderLayers(GeoBlockRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Entity implements GeoRenderEvent {
		private final GeoEntityRenderer<?> renderer;

		public Entity(GeoEntityRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public GeoEntityRenderer<?> getRenderer() {
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

			public Pre(GeoEntityRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public Post(GeoEntityRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public CompileRenderLayers(GeoEntityRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Item implements GeoRenderEvent {
		private final GeoItemRenderer<?> renderer;

		public Item(GeoItemRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public GeoItemRenderer<?> getRenderer() {
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

			public Pre(GeoItemRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public Post(GeoItemRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public CompileRenderLayers(GeoItemRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class Object implements GeoRenderEvent {
		private final GeoObjectRenderer<?> renderer;

		public Object(GeoObjectRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public GeoObjectRenderer<?> getRenderer() {
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

			public Pre(GeoObjectRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public Post(GeoObjectRenderer<?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public CompileRenderLayers(GeoObjectRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}

	
	abstract class ReplacedEntity implements GeoRenderEvent {
		private final GeoReplacedEntityRenderer<?, ?> renderer;

		public ReplacedEntity(GeoReplacedEntityRenderer<?, ?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public GeoReplacedEntityRenderer<?, ?> getRenderer() {
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

			public Pre(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public Post(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
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

			public CompileRenderLayers(GeoReplacedEntityRenderer<?, ?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}

			
			@FunctionalInterface
			public interface Listener {
				void handle(CompileRenderLayers event);
			}
		}
	}
}
