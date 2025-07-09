/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animatable;

import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.cache.item.IdCache;
import software.bluelib.oldLoader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.oldLoader.animatable.instance.SingletonAnimatableInstanceCache;
import software.bluelib.oldLoader.animation.AnimatableManager;
import software.bluelib.oldLoader.animation.ContextAwareAnimatableManager;
import software.bluelib.oldLoader.constant.DataTickets;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public interface BlueItem extends SingletonBlueAnimatable {

	static void registerSyncedAnimatable(BlueAnimatable pAnimatable) {
		SingletonBlueAnimatable.registerSyncedAnimatable(pAnimatable);
	}

	static long getId(ItemStack pStack) {
		return Optional.ofNullable(pStack.getComponentsPatch().get(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get()))
				.filter(Optional::isPresent)
				.<Long>map(Optional::get)
				.orElse(Long.MAX_VALUE);
	}

	static long getOrAssignId(ItemStack pStack, ServerLevel pLevel) {
		if (!(pStack.getComponents() instanceof PatchedDataComponentMap components))
			return Long.MAX_VALUE;

		Long id = components.get(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get());

		if (id == null)
			components.set(BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), id = IdCache.getFreeId(pLevel));

		return id;
	}

	@Override
	default double getTick(Object pItemStack) {
		return RenderUtils.getCurrentTick();
	}

	default boolean isPerspectiveAware() {
		return false;
	}

	@Nullable
	@Override
	default AnimatableInstanceCache animatableCacheOverride() {
		if (isPerspectiveAware())
			return new ContextBasedAnimatableInstanceCache(this);

		return SingletonBlueAnimatable.super.animatableCacheOverride();
	}

	class ContextBasedAnimatableInstanceCache extends SingletonAnimatableInstanceCache {

		public ContextBasedAnimatableInstanceCache(BlueAnimatable pAnimatable) {
			super(pAnimatable);
		}

		@Override
		public AnimatableManager<?> getManagerForId(long pUniqueId) {
			if (!this.managers.containsKey(pUniqueId))
				this.managers.put(pUniqueId, new ContextAwareAnimatableManager<BlueItem, ItemDisplayContext>(this.animatable) {

					@Override
					protected Map<ItemDisplayContext, AnimatableManager<BlueItem>> buildContextOptions(BlueAnimatable pAnimatable) {
						Map<ItemDisplayContext, AnimatableManager<BlueItem>> map = new EnumMap<>(ItemDisplayContext.class);

						for (ItemDisplayContext context : ItemDisplayContext.values()) {
							map.put(context, new AnimatableManager<>(pAnimatable));
						}

						return map;
					}

					@Override
					public ItemDisplayContext getCurrentContext() {
						ItemDisplayContext context = getData(DataTickets.ITEM_RENDER_PERSPECTIVE);

						return context == null ? ItemDisplayContext.NONE : context;
					}
				});

			return this.managers.get(pUniqueId);
		}
	}
}
