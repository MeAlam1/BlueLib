/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils;

import java.net.URI;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public class IsValidUtils {

	private IsValidUtils() {}

	@NotNull
	public static Boolean isValidURL(@NotNull String pUrl) {
		try {
			if (!pUrl.startsWith("http://") && !pUrl.startsWith("https://")) {
				BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("invalid_url.begin", pUrl));
				return false;
			}

			URI uri = new URI(pUrl);

			String domainRegex = "^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
			Pattern pattern = Pattern.compile(domainRegex);
			String host = uri.getHost();

			return uri.isAbsolute() && (pattern.matcher(host).matches());
		} catch (Exception pException) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("invalid_url", pUrl));
			return false;
		}
	}

	@NotNull
	public static Boolean isValidEmail(@NotNull String pEmail) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return pEmail.matches(emailRegex);
	}

	@NotNull
	public static Boolean isValidColor(@NotNull String pInput) {
		String rgbPattern = "\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\)";
		String argbPattern = "\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\)";
		String hexPattern = "^#([0-9A-Fa-f]{6})$";
		String hex0xPattern = "^0x([0-9A-Fa-f]{6})$";
		String plainHexPattern = "^([0-9A-Fa-f]{6})$";

		return pInput.matches(rgbPattern) ||
				pInput.matches(argbPattern) ||
				pInput.matches(hexPattern) ||
				pInput.matches(hex0xPattern) ||
				pInput.matches(plainHexPattern);
	}
}
