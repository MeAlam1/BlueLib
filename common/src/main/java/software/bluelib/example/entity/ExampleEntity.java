/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */

package software.bluelib.example.entity;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLangConstants;
import software.bluelib.api.molang.MoLangType;
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.loader.animatable.BlueEntity;
import software.bluelib.loader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.loader.animation.*;

public class ExampleEntity extends PathfinderMob implements BlueEntity {

    private final AnimatableInstanceCache cache = LoaderUtils.createInstanceCache(this);
    public final String entityName = "test";

    public ExampleEntity(EntityType<? extends ExampleEntity> pType, Level pLevel) {
        super(pType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes();
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        MoLangConstants.service.getRuntimeFor(MoLangType.GENERAL).evaluate("q.say('hello')");
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar pControllers) {
        pControllers.add(new AnimationController<>(this, "Idle", 5, this::idleAnimController));
    }

    protected <E extends ExampleEntity> PlayState idleAnimController(final AnimationState<E> pEvent) {
        return pEvent.setAndContinue(RawAnimation.begin().thenLoop("animation.bulbasaur.ground_idle"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
