/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.armor;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Collection;
import java.util.Set;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.Color;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.animatable.BlueItem;
import software.bluelib.oldLoader.model.BlueModel;

public abstract class DyeableBlueArmorRenderer<T extends Item & BlueItem> extends BlueArmorRenderer<T> {

	protected final Set<BoneCache> dyeableBones = new ObjectArraySet<>();
	protected ModelCache lastModel = null;

	public DyeableBlueArmorRenderer(BlueModel<T> pModel) {
		super(pModel);
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		super.preRender(pContext);

		if (!pContext.isReRender())
			checkBoneDyeCache(pContext.animatable(), pContext.model(), pContext.partialTick(), pContext.packedLight(), pContext.packedOverlay(), pContext.color());
	}

	@Override
	public void renderCubesOfBone(BoneCache pBone, FullRenderContext<T> pContext) {
		if (this.dyeableBones.contains(pBone)) {
			final Color color = getColorForBone(pBone);

			pContext.setColor(FastColor.ARGB32.multiply(pContext.color(), color.argbInt()));
		}

		super.renderCubesOfBone(pBone, pContext);
	}

	protected abstract boolean isBoneDyeable(BoneCache pBone);

	@NotNull
	protected abstract Color getColorForBone(BoneCache pBone);

	protected void checkBoneDyeCache(T pAnimatable, ModelCache pModel, float pPartialTick, int pPackedLight, int pPackedOverlay, int pColour) {
		if (pModel != this.lastModel) {
			this.dyeableBones.clear();
			this.lastModel = pModel;
			collectDyeableBones(pModel.topLevelBones());
		}
	}

	protected void collectDyeableBones(Collection<BoneCache> pBones) {
		for (BoneCache bone : pBones) {
			if (isBoneDyeable(bone))
				this.dyeableBones.add(bone);

			collectDyeableBones(bone.getChildBones());
		}
	}
}
