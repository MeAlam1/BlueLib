/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import static software.bluelib.BlueLibConstants.SCHEDULER;

import java.util.concurrent.TimeUnit;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.api.event.mod.ModIntegration;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.internal.registry.*;
import software.bluelib.internal.registry.molang.BlueMoLangContextRegistry;
import software.bluelib.client.internal.registry.BlueClientNetworkRegistry;

@ApiStatus.Internal
public class BlueLibCommon {

	private BlueLibCommon() {
	}

	public static void init() {
		if (isDeveloperMode()) {
			SCHEDULER.schedule(() -> {
				ModIntegration.checkSupportMods();
				BaseLogger.logBlueLib(Component.literal("**************************************************"));
				BaseLogger.logBlueLib(Component.literal("                                                  "));
				BaseLogger.logBlueLib(BlueTranslation.translate("mod.thank_you"));
				BaseLogger.logBlueLib(BlueTranslation.translate("mod.thank_you.subtitle"));
				BaseLogger.logBlueLib(Component.literal("                                                  "));
				BaseLogger.logBlueLib(Component.literal("**************************************************"));
				SCHEDULER.shutdown();
			}, 5, TimeUnit.SECONDS);
		}
	}

	public static void doRegistration() {
		BlueLibConstants.init();
		MixinBootstrap.init();
		InternalNetworkRegistry.registerC2SNetwork();
		InternalNetworkRegistry.registerS2CNetwork();
		BlueEntityRegistry.init();
		BlueRecipeTypeRegistry.init();
		BlueRecipeSerializerRegistry.init();
		BlueMoLangContextRegistry.init();
		BlueDataComponentRegistry.init();
	}

	public static void doClientRegistration() {
		InternalNetworkRegistry.registerC2SNetwork();
		InternalNetworkRegistry.registerS2CNetwork();
	}

	@NotNull
	public static Boolean isDeveloperMode() {
		boolean isDevMode = BlueLibConstants.PlatformHelper.PLATFORM.isDevelopmentEnvironment();
		if (isDevMode) {
			BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("Running in Developer mode."));
		}
		return isDevMode;
	}

	protected static class InternalNetworkRegistry {

		private static void registerC2SNetwork() {
			NetworkRegistry.registerC2SPacketProvider(new BlueNetworkRegistry());
		}

		private static void registerS2CNetwork() {
			NetworkRegistry.registerS2CPacketProvider(new BlueClientNetworkRegistry());

		}
	}
}
