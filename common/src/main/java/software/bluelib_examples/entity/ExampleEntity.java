
package software.bluelib_examples.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.entity.variant.IVariantEntity;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.loader.animatable.cache.AnimatableInstanceCache;
import software.bluelib.loader.animatable.entity.BlueEntity;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib_examples.BlueLibConstants;

public class ExampleEntity extends PathfinderMob implements BlueEntity, IVariantEntity<ExampleEntity> {

	public final String entityName = "test";

	public ExampleEntity(EntityType<? extends ExampleEntity> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createMobAttributes();
	}

	@Override
	public @NotNull ExampleEntity getEntity() {
		return this;
	}

	@Override
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
		NetworkRegistry.sendToAllPlayers(pLevel.getServer(), new OpenLoggerPacket());
		if (getVariantName().isEmpty()) {
			setVariantName(getRandomVariant(getEntityVariants(ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, entityName)), "normal"));
		}
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
	}

	@Override
	public @NotNull ResourceLocation getControllerResource() {
		return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, entityName + ".json");
	}
}
