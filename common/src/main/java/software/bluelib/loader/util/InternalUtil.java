/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.util;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.animatable.client.GeoRenderProvider;
import software.bluelib.loader.renderer.GeoArmorRenderer;

@SuppressWarnings("unused")
@ApiStatus.Internal
public class InternalUtil {

    public static <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> boolean tryRenderGeoArmorPiece(PoseStack pPoseStack, MultiBufferSource pBufferSource, T pEntity, ItemStack pStack, EquipmentSlot pEquipmentSlot, M pParentModel, A pBaseModel,
            float pPartialTick, int pPackedLight, float pLimbSwing, float pLimbSwingAmount, float pLerpedTickCount, float pNetHeadYaw, float pHeadPitch,
            BiConsumer<A, EquipmentSlot> pPartVisibilitySetter) {
        final Item item = pStack.getItem();

        if (!(item instanceof Equipable equipable) || equipable.getEquipmentSlot() != pEquipmentSlot)
            return false;

        final HumanoidModel<?> geckolibModel = GeoRenderProvider.of(item).getGeoArmorRenderer(pEntity, pStack, pEquipmentSlot, pBaseModel);

        if (geckolibModel == null)
            return false;

        pParentModel.copyPropertiesTo(pBaseModel);
        pPartVisibilitySetter.accept(pBaseModel, pEquipmentSlot);

        if (geckolibModel instanceof GeoArmorRenderer<?> geoArmorRenderer)
            geoArmorRenderer.prepForRender(pEntity, pStack, pEquipmentSlot, pBaseModel, pBufferSource, pPartialTick, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch);

        pBaseModel.copyPropertiesTo((A) geckolibModel);
        geckolibModel.renderToBuffer(pPoseStack, null, pPackedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());

        return true;
    }

    public static boolean areComponentsMatchingIgnoringGeckoLibId(PatchedDataComponentMap pComponentMap, PatchedDataComponentMap pComponentMapTwo) {
        final DataComponentType<Long> stackId = BlueLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get();
        boolean patched = false;

        if (pComponentMap.has(stackId)) {
            PatchedDataComponentMap prevMap = pComponentMap;
            boolean copyOnWrite = prevMap.copyOnWrite;
            (pComponentMap = pComponentMap.copy()).remove(stackId);
            pComponentMap.copyOnWrite = copyOnWrite;
            patched = true;
        }

        if (pComponentMapTwo.has(stackId)) {
            PatchedDataComponentMap prevMap = pComponentMapTwo;
            boolean copyOnWrite = prevMap.copyOnWrite;
            (pComponentMapTwo = pComponentMapTwo.copy()).remove(stackId);
            pComponentMapTwo.copyOnWrite = copyOnWrite;
            patched = true;
        }

        return patched && Objects.equals(pComponentMap, pComponentMapTwo);
    }
}
