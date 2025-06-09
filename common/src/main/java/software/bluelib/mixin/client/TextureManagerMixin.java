/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.client.loader.cache.texture.AnimatableTexture;

@Mixin(value = TextureManager.class, priority = 2000)
public abstract class TextureManagerMixin {

    @Shadow
    public abstract void register(ResourceLocation path, AbstractTexture texture);

    @WrapOperation(method = "getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/AbstractTexture;", at = @At(value = "NEW", target = "(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/SimpleTexture;"), require = 0)
    private SimpleTexture geckolib$replaceAnimatableTexture(ResourceLocation location, Operation<SimpleTexture> original) {
        AnimatableTexture animatableTexture = new AnimatableTexture(location);

        register(location, animatableTexture);

        return animatableTexture.isAnimated() ? animatableTexture : new SimpleTexture(location);
    }

    @WrapWithCondition(method = "getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/AbstractTexture;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;register(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/renderer/texture/AbstractTexture;)V"), require = 0)
    private boolean geckolib$skipAnimatableTextureRegistration(TextureManager textureManager, ResourceLocation location, AbstractTexture texture) {
        return !(texture instanceof AnimatableTexture);
    }
}
