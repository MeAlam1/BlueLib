/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.texture;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.client.utils.TextureUtils;

public class AnimatableTexture extends SimpleTexture {

	protected AnimationContents animationContents = null;
	protected boolean isAnimated = false;

	public AnimatableTexture(final ResourceLocation pLocation) {
		super(pLocation);
	}

	@Override
	public void load(ResourceManager pManager) throws IOException {
		Resource resource = pManager.getResourceOrThrow(this.location);
		AnimationMetadataSection animMeta = resource.metadata().getSection(AnimationMetadataSection.SERIALIZER).orElse(null);

		if (animMeta != null) {
			NativeImage nativeImage;

			try (InputStream inputstream = resource.open()) {
				nativeImage = NativeImage.read(inputstream);
			}

			this.animationContents = new AnimationContents(nativeImage, animMeta);

			if (!this.animationContents.isValid()) {
				nativeImage.close();

				return;
			}

			this.isAnimated = true;

			onRenderThread(() -> {
				TextureUtil.prepareImage(getId(), 0, this.animationContents.frameSize.width(), this.animationContents.frameSize.height());
				nativeImage.upload(0, 0, 0, 0, 0, this.animationContents.frameSize.width(), this.animationContents.frameSize.height(), false, false);
			});
		}
	}

	public boolean isAnimated() {
		return this.isAnimated;
	}

	public static void setAndUpdate(ResourceLocation pTexturePath) {
		setAndUpdate(pTexturePath, RenderUtils.getCurrentTick().intValue());
	}

	public static void setAndUpdate(ResourceLocation pTexturePath, int pFrameTick) {
		AbstractTexture texture = TextureUtils.getTexture(pTexturePath);

		if (texture instanceof AnimatableTexture animatableTexture)
			animatableTexture.setAnimationFrame(pFrameTick);

		RenderSystem.setShaderTexture(0, texture.getId());
	}

	public void setAnimationFrame(int pTick) {
		if (this.animationContents != null && this.animationContents.animatedTexture != null)
			this.animationContents.animatedTexture.setCurrentFrame(pTick);
	}

	private static void onRenderThread(RenderCall pRenderCall) {
		if (!RenderSystem.isOnRenderThread()) {
			RenderSystem.recordRenderCall(pRenderCall);
		} else {
			pRenderCall.execute();
		}
	}

	protected class AnimationContents {

		protected final FrameSize frameSize;
		protected final Texture animatedTexture;

		private AnimationContents(NativeImage pImage, AnimationMetadataSection pAnimMeta) {
			this.frameSize = pAnimMeta.calculateFrameSize(pImage.getWidth(), pImage.getHeight());
			this.animatedTexture = generateAnimatedTexture(pImage, pAnimMeta);
		}

		private boolean isValid() {
			return this.animatedTexture != null;
		}

		private Texture generateAnimatedTexture(NativeImage pImage, AnimationMetadataSection pAnimMeta) {
			if (!Mth.isMultipleOf(pImage.getWidth(), this.frameSize.width()) || !Mth.isMultipleOf(pImage.getHeight(), this.frameSize.height())) {
				//BlueLibConstants.LOGGER.error("Image {} size {},{} is not multiple of frame size {},{}", AnimatableTexture.this.location, image.getWidth(), image.getHeight(), this.frameSize.width(), this.frameSize.height());

				return null;
			}

			int columns = pImage.getWidth() / this.frameSize.width();
			int rows = pImage.getHeight() / this.frameSize.height();
			int frameCount = columns * rows;
			List<Frame> frames = new ObjectArrayList<>();

			pAnimMeta.forEachFrame((frame, frameTime) -> frames.add(new Frame(frame, frameTime)));

			if (frames.isEmpty()) {
				for (int frame = 0; frame < frameCount; ++frame) {
					frames.add(new Frame(frame, pAnimMeta.getDefaultFrameTime()));
				}
			} else {
				int index = 0;
				IntSet unusedFrames = new IntOpenHashSet();

				for (Frame frame : frames) {
					if (frame.time <= 0) {
						//BlueLibConstants.LOGGER.warn("Invalid frame duration on sprite {} frame {}: {}", AnimatableTexture.this.location, index, frame.time);
						unusedFrames.add(frame.index);
					} else if (frame.index < 0 || frame.index >= frameCount) {
						//BlueLibConstants.LOGGER.warn("Invalid frame index on sprite {} frame {}: {}", AnimatableTexture.this.location, index, frame.index);
						unusedFrames.add(frame.index);
					}

					index++;
				}

				//if (!unusedFrames.isEmpty())
				//BlueLibConstants.LOGGER.warn("Unused frames in sprite {}: {}", AnimatableTexture.this.location, Arrays.toString(unusedFrames.toArray()));
			}

			return frames.size() <= 1 ? null : new Texture(pImage, frames.toArray(new Frame[0]), columns, pAnimMeta.isInterpolatedFrames());
		}

		protected record Frame(int index, int time) {}

		protected class Texture implements AutoCloseable {

			protected final NativeImage baseImage;
			protected final Frame[] frames;
			protected final int framePanelSize;
			protected final boolean interpolating;
			protected final NativeImage interpolatedFrame;
			protected final int totalFrameTime;

			protected int glowMaskTextureId = -1;
			protected NativeImage glowmaskImage = null;
			protected NativeImage glowmaskInterpolatedFrame = null;

			protected int currentFrame;
			protected int currentSubframe;

			private Texture(NativeImage pBaseImage, Frame[] pFrames, int pFramePanelSize, boolean pInterpolating) {
				this.baseImage = pBaseImage;
				this.frames = pFrames;
				this.framePanelSize = pFramePanelSize;
				this.interpolating = pInterpolating;
				this.interpolatedFrame = pInterpolating ? new NativeImage(AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false) : null;
				int time = 0;

				for (Frame frame : this.frames) {
					time += frame.time;
				}

				this.totalFrameTime = time;
			}

			private int getFrameX(int pFrameIndex) {
				return pFrameIndex % this.framePanelSize;
			}

			private int getFrameY(int pFrameIndex) {
				return pFrameIndex / this.framePanelSize;
			}

			public void setGlowMaskTexture(AutoGlowingTexture pTexture, NativeImage pBaseImage, NativeImage pGlowMask) {
				this.glowMaskTextureId = pTexture.getId();
				this.glowmaskImage = pGlowMask;
				this.glowmaskInterpolatedFrame = this.interpolating ? new NativeImage(AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false) : null;
				this.baseImage.copyFrom(pBaseImage);
			}

			public void setCurrentFrame(int pTicks) {
				pTicks %= this.totalFrameTime;

				if (pTicks == this.currentSubframe)
					return;

				int lastSubframe = this.currentSubframe;
				int lastFrame = this.currentFrame;
				int time = 0;

				for (Frame frame : this.frames) {
					time += frame.time;

					if (pTicks < time) {
						this.currentFrame = frame.index;
						this.currentSubframe = pTicks % frame.time;

						break;
					}
				}

				if (this.currentFrame != lastFrame && this.currentSubframe == 0) {
					onRenderThread(() -> {
						TextureUtil.prepareImage(getId(), 0, AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height());
						this.baseImage.upload(0, 0, 0, getFrameX(this.currentFrame) * AnimationContents.this.frameSize.width(), getFrameY(this.currentFrame) * AnimationContents.this.frameSize.height(), AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false, false);

						if (this.glowmaskImage != null) {
							TextureUtil.prepareImage(this.glowMaskTextureId, 0, AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height());
							this.glowmaskImage.upload(0, 0, 0, getFrameX(this.currentFrame) * AnimationContents.this.frameSize.width(), getFrameY(this.currentFrame) * AnimationContents.this.frameSize.height(), AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false, false);
						}
					});
				} else if (this.currentSubframe != lastSubframe && this.interpolating) {
					onRenderThread(() -> {
						generateInterpolatedFrame(getId(), this.baseImage, this.interpolatedFrame);

						if (this.glowmaskImage != null)
							generateInterpolatedFrame(this.glowMaskTextureId, this.glowmaskImage, this.glowmaskInterpolatedFrame);
					});
				}
			}

			private void generateInterpolatedFrame(int pTextureId, NativeImage pImage, NativeImage pInterpolatedFrame) {
				Frame frame = this.frames[this.currentFrame];
				double frameProgress = 1 - (double) this.currentSubframe / (double) frame.time;
				int nextFrameIndex = this.frames[(this.currentFrame + 1) % this.frames.length].index;

				if (frame.index != nextFrameIndex) {
					for (int y = 0; y < pInterpolatedFrame.getHeight(); ++y) {
						for (int x = 0; x < pInterpolatedFrame.getWidth(); ++x) {
							int prevFramePixel = getPixel(pImage, frame.index, x, y);
							int nextFramePixel = getPixel(pImage, nextFrameIndex, x, y);
							int blendedRed = interpolate(frameProgress, prevFramePixel >> 16 & 255, nextFramePixel >> 16 & 255);
							int blendedGreen = interpolate(frameProgress, prevFramePixel >> 8 & 255, nextFramePixel >> 8 & 255);
							int blendedBlue = interpolate(frameProgress, prevFramePixel & 255, nextFramePixel & 255);

							pInterpolatedFrame.setPixelRGBA(x, y, prevFramePixel & -16777216 | blendedRed << 16 | blendedGreen << 8 | blendedBlue);
						}
					}

					TextureUtil.prepareImage(pTextureId, 0, AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height());
					pInterpolatedFrame.upload(0, 0, 0, 0, 0, AnimationContents.this.frameSize.width(), AnimationContents.this.frameSize.height(), false, false);
				}
			}

			private int getPixel(NativeImage pImage, int pFrameIndex, int pX, int pY) {
				return pImage.getPixelRGBA(pX + getFrameX(pFrameIndex) * AnimationContents.this.frameSize.width(), pY + getFrameY(pFrameIndex) * AnimationContents.this.frameSize.height());
			}

			private int interpolate(double pFrameProgress, double pPrevColor, double pNextColor) {
				return (int) (pFrameProgress * pPrevColor + (1 - pFrameProgress) * pNextColor);
			}

			@Override
			public void close() {
				this.baseImage.close();

				if (this.interpolatedFrame != null)
					this.interpolatedFrame.close();

				if (this.glowmaskImage != null)
					this.glowmaskImage.close();

				if (this.glowmaskInterpolatedFrame != null)
					this.glowmaskInterpolatedFrame.close();
			}
		}
	}
}
