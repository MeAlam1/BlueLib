/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */

package software.bluelib.example.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.MoLang;
import software.bluelib.api.molang.MoLangUtils;
import software.bluelib.internal.BlueResource;
import software.bluelib.loader.animatable.entity.BlueEntity;

public class ExampleEntity extends PathfinderMob implements BlueEntity {

	public final String entityName = "test";

	public ExampleEntity(EntityType<? extends ExampleEntity> pType, Level pLevel) {
		super(pType, pLevel);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createMobAttributes();
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		MoLang.evaluate("q.print(q.add(1, 2, 3))");
		System.out.println("Add: " + MoLang.evaluate("q.add(1, 2, 3)"));
		System.out.println("Random: " + MoLang.evaluate("q.math.random_int(0,1)"));
		System.out.println("Equals Random: " + MoLang.evaluate("q.equals(q.math.random_int(0,1), 1)"));
		System.out.println("Equals: " + MoLangUtils.entity("q.equals(q.get_health, 1)", this));
		return super.mobInteract(player, hand);
	}

	@Override
	public @NotNull ResourceLocation getControllerResource() {
		return BlueResource.resource("controller/test.controller.json");
	}
}
