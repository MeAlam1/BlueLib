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
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.Color;
import software.bluelib.loader.animatable.item.BlueItem;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;

public abstract class DyeableBlueArmorRenderer<T extends Item & BlueItem> extends BlueArmorRenderer<T> {

	@NotNull
	protected final Set<BoneCache> dyeableBones = new ObjectArraySet<>();
	@Nullable
	protected ModelCache lastModel = null;

	public DyeableBlueArmorRenderer(@NotNull BlueModel<T> pModel) {
		super(pModel);
	}

	@Override
	public void preRender(@NotNull IRenderContext<T> pContext) {
		super.preRender(pContext);

		if (!pContext.isReRender())
			checkBoneDyeCache(pContext);
	}

	@Override
	public void renderCubesOfBone(@NotNull BoneCache pBone, @NotNull FullRenderContext<T> pContext) {
		if (this.dyeableBones.contains(pBone)) {
			final Color color = getColorForBone(pBone);

			pContext.setColor(FastColor.ARGB32.multiply(pContext.color(), color.argbInt()));
		}

		super.renderCubesOfBone(pBone, pContext);
	}

	protected abstract boolean isBoneDyeable(@NotNull BoneCache pBone);

	@NotNull
	protected abstract Color getColorForBone(@NotNull BoneCache pBone);

	protected void checkBoneDyeCache(@NotNull IRenderContext<T> pContext) {
		if (pContext.model() != this.lastModel) {
			this.dyeableBones.clear();
			this.lastModel = pContext.model();
			collectDyeableBones(pContext.model().topLevelBones());
		}
	}

	protected void collectDyeableBones(@NotNull Collection<BoneCache> pBones) {
		for (BoneCache bone : pBones) {
			if (isBoneDyeable(bone))
				this.dyeableBones.add(bone);

			collectDyeableBones(bone.getChildBones());
		}
	}
}
