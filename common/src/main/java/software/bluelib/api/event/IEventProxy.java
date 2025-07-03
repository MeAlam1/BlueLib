/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.oldLoader.renderer.*;

public interface IEventProxy {

	// Mod Events

	void onModLoaded(@NotNull ModMeta pModData);

	void onAllModsLoaded(@NotNull List<ModMeta> pModData);

	// Variant Events

	@NotNull
	Boolean variantLoadedPre(@NotNull String pEntityName, @NotNull String pVariant);

	void variantLoadedPost(@NotNull String pEntityName, @NotNull String pVariant);

	@NotNull
	Boolean allVariantsLoadedPre(@NotNull String pEntityName);

	void allVariantsLoadedPost(@NotNull String pEntityName);

	// Render Events

	// Block
	void fireCompileBlockRenderLayers(@NotNull BlueBlockRenderer<?> pRenderer);

	@NotNull
	Boolean fireBlockPreRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	void fireBlockPostRender(@NotNull BlueBlockRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	// Armor

	void fireCompileArmorRenderLayers(@NotNull BlueArmorRenderer<?> pRenderer);

	@NotNull
	Boolean fireArmorPreRender(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	void fireArmorPostRender(@NotNull BlueArmorRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	// Entity

	void fireCompileEntityRenderLayers(@NotNull BlueEntityRenderer<?> pRenderer);

	@NotNull
	Boolean fireEntityPreRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	void fireEntityPostRender(@NotNull BlueEntityRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	void fireCompileReplacedEntityRenderLayers(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer);

	@NotNull
	Boolean fireReplacedEntityPreRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	void fireReplacedEntityPostRender(@NotNull BlueReplacedEntityRenderer<?, ?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	// Item

	void fireCompileItemRenderLayers(@NotNull BlueItemRenderer<?> pRenderer);

	@NotNull
	Boolean fireItemPreRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	void fireItemPostRender(@NotNull BlueItemRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	// Object

	void fireCompileObjectRenderLayers(@NotNull BlueObjectRenderer<?> pRenderer);

	@NotNull
	Boolean fireObjectPreRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);

	void fireObjectPostRender(@NotNull BlueObjectRenderer<?> pRenderer, @NotNull PoseStack pPoseStack, @NotNull ModelCache pModel, @NotNull MultiBufferSource pBufferSource, @NotNull Float pPartialTick, @NotNull Integer pPackedLight);
}
