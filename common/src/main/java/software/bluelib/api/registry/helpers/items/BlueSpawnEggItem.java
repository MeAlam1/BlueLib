package software.bluelib.api.registry.helpers.items;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BlueSpawnEggItem extends Item {

	private static final Map<EntityType<? extends Entity>, BlueSpawnEggItem> BY_ID = Maps.newIdentityHashMap();
	private static final MapCodec<EntityType<?>> ENTITY_TYPE_FIELD_CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id");
	private final int backgroundColor;
	private final int highlightColor;
	private final EntityType<?> defaultType;

	public BlueSpawnEggItem(EntityType<? extends Mob> defaultType, int backgroundColor, int highlightColor, Item.Properties properties) {
		super(properties);
		this.defaultType = defaultType;
		this.backgroundColor = backgroundColor;
		this.highlightColor = highlightColor;
		BY_ID.put(defaultType, this);
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack itemstack = context.getItemInHand();
			BlockPos blockpos = context.getClickedPos();
			Direction direction = context.getClickedFace();
			BlockState blockstate = level.getBlockState(blockpos);
			if (level.getBlockEntity(blockpos) instanceof Spawner spawner) {
				EntityType<?> entitytype1 = this.getType(itemstack);
				spawner.setEntityId(entitytype1, level.getRandom());
				level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
				level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
				itemstack.shrink(1);
			} else {
				BlockPos blockpos1;
				if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
					blockpos1 = blockpos;
				} else {
					blockpos1 = blockpos.relative(direction);
				}

				EntityType<?> entitytype = this.getType(itemstack);
				if (entitytype.spawn(
						(ServerLevel) level,
						itemstack,
						context.getPlayer(),
						blockpos1,
						MobSpawnType.SPAWN_EGG,
						true,
						!Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
					itemstack.shrink(1);
					level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
				}

			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		if (blockhitresult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemstack);
		} else if (!(level instanceof ServerLevel)) {
			return InteractionResultHolder.success(itemstack);
		} else {
			BlockPos blockpos = blockhitresult.getBlockPos();
			if (!(level.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
				return InteractionResultHolder.pass(itemstack);
			} else if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, blockhitresult.getDirection(), itemstack)) {
				EntityType<?> entitytype = this.getType(itemstack);
				Entity entity = entitytype.spawn((ServerLevel) level, itemstack, player, blockpos, MobSpawnType.SPAWN_EGG, false, false);
				if (entity == null) {
					return InteractionResultHolder.pass(itemstack);
				} else {
					itemstack.consume(1, player);
					player.awardStat(Stats.ITEM_USED.get(this));
					level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
					return InteractionResultHolder.consume(itemstack);
				}
			} else {
				return InteractionResultHolder.fail(itemstack);
			}
		}
	}

	public boolean spawnsEntity(ItemStack stack, EntityType<?> entityType) {
		return Objects.equals(this.getType(stack), entityType);
	}

	public int getColor(int tintIndex) {
		return tintIndex == 0 ? this.backgroundColor : this.highlightColor;
	}

	@Nullable
	public static BlueSpawnEggItem byId(@Nullable EntityType<?> type) {
		return BY_ID.get(type);
	}

	public static Iterable<BlueSpawnEggItem> eggs() {
		return Iterables.unmodifiableIterable(BY_ID.values());
	}

	public EntityType<?> getType(ItemStack stack) {
		CustomData customdata = stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
		return !customdata.isEmpty() ? customdata.read(ENTITY_TYPE_FIELD_CODEC).result().orElse(this.defaultType) : this.defaultType;
	}

	@Override
	public @NotNull FeatureFlagSet requiredFeatures() {
		return this.defaultType.requiredFeatures();
	}

	public Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob p_mob, EntityType<? extends Mob> entityType, ServerLevel serverLevel, Vec3 pos, ItemStack stack) {
		if (!this.spawnsEntity(stack, entityType)) {
			return Optional.empty();
		} else {
			Mob mob;
			if (p_mob instanceof AgeableMob) {
				mob = ((AgeableMob) p_mob).getBreedOffspring(serverLevel, (AgeableMob) p_mob);
			} else {
				mob = entityType.create(serverLevel);
			}

			if (mob == null) {
				return Optional.empty();
			} else {
				mob.setBaby(true);
				if (!mob.isBaby()) {
					return Optional.empty();
				} else {
					mob.moveTo(pos.x(), pos.y(), pos.z(), 0.0F, 0.0F);
					serverLevel.addFreshEntityWithPassengers(mob);
					mob.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
					stack.consume(1, player);
					return Optional.of(mob);
				}
			}
		}
	}
}
