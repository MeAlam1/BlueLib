package software.bluelib.loader.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.renderer.*;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;


public interface GeoRenderEvent {
	
	GeoRenderer<?> getRenderer();

	
	abstract class Armor extends Event implements GeoRenderEvent {
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

		
		public static class Pre extends Armor implements ICancellableEvent {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Pre(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class Post extends Armor {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Post(GeoArmorRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class CompileRenderLayers extends Armor {
			public CompileRenderLayers(GeoArmorRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}
		}
	}

	
	abstract class Block extends Event implements GeoRenderEvent {
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

		
		public static class Pre extends Block implements ICancellableEvent {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Pre(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class Post extends Block {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Post(GeoBlockRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class CompileRenderLayers extends Block {
			public CompileRenderLayers(GeoBlockRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}
		}
	}

	
	abstract class Entity extends Event implements GeoRenderEvent {
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

		
		public static class Pre extends Entity implements ICancellableEvent {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Pre(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class Post extends Entity {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Post(GeoEntityRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class CompileRenderLayers extends Entity {
			public CompileRenderLayers(GeoEntityRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}
		}
	}

	
	abstract class Item extends Event implements GeoRenderEvent {
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

		
		public static class Pre extends Item implements ICancellableEvent {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Pre(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class Post extends Item {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Post(GeoItemRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class CompileRenderLayers extends Item {
			public CompileRenderLayers(GeoItemRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}
		}
	}

	
	abstract class Object extends Event implements GeoRenderEvent {
		private final GeoObjectRenderer<?> renderer;

		public Object(GeoObjectRenderer<?> renderer) {
			this.renderer = renderer;
		}

		
		@Override
		public GeoObjectRenderer<?> getRenderer() {
			return this.renderer;
		}

		
		public static class Pre extends Object implements ICancellableEvent {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Pre(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class Post extends Object {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Post(GeoObjectRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class CompileRenderLayers extends Object {
			public CompileRenderLayers(GeoObjectRenderer<?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}
		}
	}

	
	abstract class ReplacedEntity extends Event implements GeoRenderEvent {
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

		
		public static class Pre extends ReplacedEntity implements ICancellableEvent {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Pre(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}
			
			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class Post extends ReplacedEntity {
			private final PoseStack poseStack;
			private final BakedGeoModel model;
			private final MultiBufferSource bufferSource;
			private final float partialTick;
			private final int packedLight;

			public Post(GeoReplacedEntityRenderer<?, ?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
				super(renderer);

				this.poseStack = poseStack;
				this.model = model;
				this.bufferSource = bufferSource;
				this.partialTick = partialTick;
				this.packedLight = packedLight;
			}

			public PoseStack getPoseStack() {
				return this.poseStack;
			}

			public BakedGeoModel getModel() {
				return this.model;
			}

			public MultiBufferSource getBufferSource() {
				return this.bufferSource;
			}

			public float getPartialTick() {
				return this.partialTick;
			}

			public int getPackedLight() {
				return this.packedLight;
			}
		}

		
		public static class CompileRenderLayers extends ReplacedEntity {
			public CompileRenderLayers(GeoReplacedEntityRenderer<?, ?> renderer) {
				super(renderer);
			}

			
			public void addLayer(GeoRenderLayer renderLayer) {
				getRenderer().addRenderLayer(renderLayer);
			}
		}
	}
}
