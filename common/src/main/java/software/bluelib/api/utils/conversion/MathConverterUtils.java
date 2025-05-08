// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class MathConverterUtils {

    private MathConverterUtils() {}

    public static double inchesToCentimeters(double pInches) {
        return pInches * 2.54;
    }

    public static double centimetersToInches(double pCentimeters) {
        return pCentimeters / 2.54;
    }

    public static double celsiusToFahrenheit(double pCelsius) {
        return pCelsius * 9 / 5 + 32;
    }

    public static double fahrenheitToCelsius(double pFahrenheit) {
        return (pFahrenheit - 32) * 5 / 9;
    }

    public static double kilometersToMiles(double pKilometers) {
        return pKilometers * 0.621371;
    }

    public static double milesToKilometers(double pMiles) {
        return pMiles / 0.621371;
    }

    public static Date stringToDate(String pDateStr, String pFormat) throws ParseException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
            return formatter.parse(pDateStr);
        } catch (ParseException pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("error.date.format", pDateStr, pFormat), pException, true);
            throw pException;
        }
    }

    public static String dateToString(Date pDate, String pFormat) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
            return formatter.format(pDate);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("error.date.format", pDate, pFormat), pException, true);
            return pException.getMessage();
        }
    }
}
