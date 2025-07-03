/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public class MathConverterUtils {

	private MathConverterUtils() {}

	public static @NotNull Double inchesToCentimeters(@NotNull Double pInches) {
		return pInches * 2.54;
	}

	public static @NotNull Double centimetersToInches(@NotNull Double pCentimeters) {
		return pCentimeters / 2.54;
	}

	public static @NotNull Double celsiusToFahrenheit(@NotNull Double pCelsius) {
		return pCelsius * 9 / 5 + 32;
	}

	public static @NotNull Double fahrenheitToCelsius(@NotNull Double pFahrenheit) {
		return (pFahrenheit - 32) * 5 / 9;
	}

	public static @NotNull Double kilometersToMiles(@NotNull Double pKilometers) {
		return pKilometers * 0.621371;
	}

	public static @NotNull Double milesToKilometers(@NotNull Double pMiles) {
		return pMiles / 0.621371;
	}

	@NotNull
	public static Date stringToDate(@NotNull String pDateStr, @NotNull String pFormat) throws ParseException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
			return formatter.parse(pDateStr);
		} catch (ParseException pException) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("error.date.format", pDateStr, pFormat), pException);
			throw pException;
		}
	}

	@NotNull
	public static String dateToString(@NotNull Date pDate, @NotNull String pFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
			return formatter.format(pDate);
		} catch (Exception pException) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("error.date.format", pDate, pFormat), pException);
			return pException.getMessage();
		}
	}
}
