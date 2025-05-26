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
    void fireCompileBlockRenderLayers(GeoBlockRenderer<?> pRenderer);

    boolean fireBlockPreRender(GeoBlockRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireBlockPostRender(GeoBlockRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Armor

    void fireCompileArmorRenderLayers(GeoArmorRenderer<?> pRenderer);

    boolean fireArmorPreRender(GeoArmorRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireArmorPostRender(GeoArmorRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Entity

    void fireCompileEntityRenderLayers(GeoEntityRenderer<?> pRenderer);

    boolean fireEntityPreRender(GeoEntityRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireEntityPostRender(GeoEntityRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireCompileReplacedEntityRenderLayers(GeoReplacedEntityRenderer<?, ?> pRenderer);

    boolean fireReplacedEntityPreRender(GeoReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireReplacedEntityPostRender(GeoReplacedEntityRenderer<?, ?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Item

    void fireCompileItemRenderLayers(GeoItemRenderer<?> pRenderer);

    boolean fireItemPreRender(GeoItemRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireItemPostRender(GeoItemRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    // Object

    void fireCompileObjectRenderLayers(GeoObjectRenderer<?> pRenderer);

    boolean fireObjectPreRender(GeoObjectRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void fireObjectPostRender(GeoObjectRenderer<?> pRenderer, PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);
}
