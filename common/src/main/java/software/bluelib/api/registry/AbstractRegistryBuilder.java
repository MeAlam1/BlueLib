/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry;

import java.util.List;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import software.bluelib.api.registry.builders.blocks.BlockBuilder;
import software.bluelib.api.registry.builders.blocks.BlockEntityBuilder;
import software.bluelib.api.registry.builders.entity.LivingEntityBuilder;
import software.bluelib.api.registry.builders.entity.ProjectileBuilder;
import software.bluelib.api.registry.builders.items.ItemBuilder;
import software.bluelib.api.registry.builders.keybinds.KeybindBuilder;
import software.bluelib.api.registry.builders.tabs.CreativeTabBuilder;
import software.bluelib.api.registry.datagen.entity.EntityTagBuilder;

public abstract class AbstractRegistryBuilder {

	private final String modID;

	public AbstractRegistryBuilder(final String pModId) {
		modID = pModId;
	}

    public String getModID() {
		return modID;
	}

	public <T extends LivingEntity> LivingEntityBuilder<T> livingEntity(String pName, EntityType.EntityFactory<T> pFactory, MobCategory pCategory) {
		return new LivingEntityBuilder<>(pName, pFactory, pCategory, modID);
	}

	public <T extends Entity> ProjectileBuilder<T> projectile(String pName, EntityType.EntityFactory<T> pFactory, MobCategory pCategory, Class<T> pEntityClass) {
		return new ProjectileBuilder<>(pName, pFactory, pCategory, pEntityClass, modID);
	}

	public <T extends Block> BlockBuilder<T> block(String pName, Function<Block.Properties, T> pFactory) {
		return new BlockBuilder<>(pName, pFactory, modID);
	}

	public <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String pName, BlockEntityType.BlockEntitySupplier<T> pFactory) {
		return new BlockEntityBuilder<>(pName, pFactory, modID);
	}

	public <T extends Item> ItemBuilder<T> item(String pName, Function<Item.Properties, T> pConstructor) {
		return new ItemBuilder<>(pName, pConstructor, modID);
	}

    public EntityTagBuilder entityTag(String pName) {
        return new EntityTagBuilder(pName, modID);
    }

	public CreativeTabBuilder tab(String pId) {
		return new CreativeTabBuilder(pId, modID);
	}

	public KeybindBuilder keybind(String pName, int pKeyCode) {
		return new KeybindBuilder(pName, pKeyCode, modID);
	}
}
