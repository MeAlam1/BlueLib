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
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.loader.renderer.*;

public interface IEventProxy {

    // Mod Events

    void onModLoaded(ModMeta pModData);

    void onAllModsLoaded(List<ModMeta> pModData);

    // Variant Events

    boolean variantLoadedPre(String pEntityName, String pVariant);

    void variantLoadedPost(String pEntityName, String pVariant);

    boolean allVariantsLoadedPre(String pEntityName);

    void allVariantsLoadedPost(String pEntityName);

    // Render Events

    // Block
    void fireCompileBlockRenderLayers(BlueBlockRenderer<?> pRenderer);

    boolean fireBlockPreRender(BlueBlockRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireBlockPostRender(BlueBlockRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Armor

    void fireCompileArmorRenderLayers(BlueArmorRenderer<?> pRenderer);

    boolean fireArmorPreRender(BlueArmorRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireArmorPostRender(BlueArmorRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Entity

    void fireCompileEntityRenderLayers(BlueEntityRenderer<?> pRenderer);

    boolean fireEntityPreRender(BlueEntityRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireEntityPostRender(BlueEntityRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireCompileReplacedEntityRenderLayers(BlueReplacedEntityRenderer<?, ?> pRenderer);

    boolean fireReplacedEntityPreRender(BlueReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireReplacedEntityPostRender(BlueReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Item

    void fireCompileItemRenderLayers(BlueItemRenderer<?> pRenderer);

    boolean fireItemPreRender(BlueItemRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireItemPostRender(BlueItemRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Object

    void fireCompileObjectRenderLayers(BlueObjectRenderer<?> pRenderer);

    boolean fireObjectPreRender(BlueObjectRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireObjectPostRender(BlueObjectRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);
}
