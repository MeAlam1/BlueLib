/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.specialty;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.Color;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.animatable.BlueItem;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.BlueArmorRenderer;

import java.util.Collection;
import java.util.Set;

public abstract class DyeableBlueArmorRenderer<T extends Item & BlueItem> extends BlueArmorRenderer<T> {

	protected final Set<BoneCache> dyeableBones = new ObjectArraySet<>();
	protected ModelCache lastModel = null;

	public DyeableBlueArmorRenderer(BlueModel<T> model) {
		super(model);
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		super.preRender(pContext);

		if (!pContext.isReRender())
			checkBoneDyeCache(pContext.animatable(), pContext.model(), pContext.partialTick(), pContext.packedLight(), pContext.packedOverlay(), pContext.color());
	}

	@Override
	public void renderCubesOfBone(PoseStack pPoseStack, BoneCache bone, VertexConsumer buffer, int pPackedLight, int pPackedOverlay, int colour) {
		if (this.dyeableBones.contains(bone)) {
			final Color color = getColorForBone(bone);

			colour = FastColor.ARGB32.multiply(colour, color.argbInt());
		}

		super.renderCubesOfBone(pPoseStack, bone, buffer, pPackedLight, pPackedOverlay, colour);
	}

	protected abstract boolean isBoneDyeable(BoneCache bone);

	@NotNull
	protected abstract Color getColorForBone(BoneCache bone);

	protected void checkBoneDyeCache(T animatable, ModelCache model, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
		if (model != this.lastModel) {
			this.dyeableBones.clear();
			this.lastModel = model;
			collectDyeableBones(model.topLevelBones());
		}
	}

	protected void collectDyeableBones(Collection<BoneCache> bones) {
		for (BoneCache bone : bones) {
			if (isBoneDyeable(bone))
				this.dyeableBones.add(bone);

			collectDyeableBones(bone.getChildBones());
		}
	}
}
