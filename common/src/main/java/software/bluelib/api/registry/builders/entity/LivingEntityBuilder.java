/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry.builders.entity;

import java.util.*;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.datagen.items.ItemModelGenerator;
import software.bluelib.api.registry.datagen.items.ItemModelTemplates;
import software.bluelib.api.registry.helpers.entity.AttributeHelper;
import software.bluelib.api.registry.helpers.entity.RenderHelper;
import software.bluelib.api.registry.helpers.items.BlueSpawnEggItem;

public class LivingEntityBuilder<T extends LivingEntity> extends EntityBuilder<T, LivingEntityBuilder<T>> {

	private boolean hasSpawnEgg = false;
	private int primaryEggColor;
	private int secondaryEggColor;
	private Supplier<AttributeSupplier.Builder> attributeBuilder;
	private boolean hasVariants = false;
	private final List<String> entityNames = new ArrayList<>();
	private Supplier<CreativeModeTab> tabSupplier;
	private final Map<Supplier<CreativeModeTab>, List<Supplier<Item>>> spawnEggsByTab = new HashMap<>();

	public LivingEntityBuilder(String pName, EntityType.EntityFactory<T> pFactory, MobCategory pCategory, String pModId) {
		super(pName, pFactory, pCategory, pModId);
	}

	public LivingEntityBuilder<T> spawnEgg(int pPrimaryColor, int pSecondaryColor) {
		this.hasSpawnEgg = true;
		this.primaryEggColor = pPrimaryColor;
		this.secondaryEggColor = pSecondaryColor;
		return this;
	}

	public LivingEntityBuilder<T> attributes(Supplier<AttributeSupplier.Builder> pAttributes) {
		this.attributeBuilder = pAttributes;
		return this;
	}

	public LivingEntityBuilder<T> loadVariants() {
		this.hasVariants = true;
		return this;
	}

	public LivingEntityBuilder<T> tab(Supplier<CreativeModeTab> pTabSupplier) {
		this.tabSupplier = pTabSupplier;
		return this;
	}

	public List<Supplier<Item>> getSpawnEggsForTab(CreativeModeTab pTab) {
		List<Supplier<Item>> spawnEggs = new ArrayList<>();
		for (Map.Entry<Supplier<CreativeModeTab>, List<Supplier<Item>>> entry : spawnEggsByTab.entrySet()) {
			if (Objects.equals(entry.getKey().get(), pTab)) {
				spawnEggs.addAll(entry.getValue());
			}
		}
		return Collections.unmodifiableList(spawnEggs);
	}

	@Override
	public Supplier<EntityType<T>> register() {
		Supplier<EntityType<T>> entityTypeSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerEntity(
				name,
				() -> EntityType.Builder.of(factory, category).sized(width, height).build(name));

		if (hasSpawnEgg && Mob.class.isAssignableFrom(factory.getClass())) {
			registerSpawnEgg(name, (Supplier<EntityType<? extends Mob>>) (Supplier<?>) entityTypeSupplier,
					primaryEggColor, secondaryEggColor, tabSupplier);
		}

		if (attributeBuilder != null) {
			new AttributeHelper().queueAttributes(entityTypeSupplier, attributeBuilder);
		}

		if (rendererProvider != null) {
            new RenderHelper().queueRenderer((entityConsumer, blockConsumer) -> entityConsumer.accept(entityTypeSupplier.get(), rendererProvider));
		}

		if (hasVariants) {
			entityNames.add(name);
		}

		return entityTypeSupplier;
	}

	public void doSpawnEggDatagen(String pModId) {
		if (hasSpawnEgg) {
			String spawnEggName = name + "_spawn_egg";
			ItemModelGenerator.generateItemModel(pModId, spawnEggName, ItemModelTemplates.SPAWN_EGG);
		}
	}

	public List<String> getEntityNames() {
		return Collections.unmodifiableList(entityNames);
	}

	public <U extends Mob> Supplier<Item> registerSpawnEgg(
			String pName,
			Supplier<EntityType<? extends Mob>> pEntityType,
			int pPrimaryColor,
			int pSecondaryColor,
			Supplier<CreativeModeTab> pTabSupplier) {
		hasSpawnEgg = true;
		Supplier<Item> spawnEggSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerItem(
				pName + "_spawn_egg",
				() -> new BlueSpawnEggItem(
						pEntityType.get(),
						pPrimaryColor,
						pSecondaryColor,
						new Item.Properties()));
		if (pTabSupplier != null) {
			spawnEggsByTab.computeIfAbsent(pTabSupplier, k -> new ArrayList<>()).add(spawnEggSupplier);
		}
		return spawnEggSupplier;
	}

	@Override
	protected LivingEntityBuilder<T> self() {
		return this;
	}
}
