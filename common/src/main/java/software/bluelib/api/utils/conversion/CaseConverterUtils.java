/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.conversion;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public class CaseConverterUtils {

    private CaseConverterUtils() {}

    @NotNull
    public static String toCamelCase(@NotNull String pInput) {
        if (Character.isUpperCase(pInput.charAt(0)) && !pInput.contains("_") && !pInput.contains("-")) {
            return pInput.substring(0, 1).toLowerCase() + pInput.substring(1);
        }

        if (pInput.contains("_")) {
            return convertUsingDelimiter(pInput, "_", true);
        }

        if (pInput.contains("-")) {
            return convertUsingDelimiter(pInput, "-", true);
        }

        BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.translate("notfound"));
        return pInput;
    }

    @NotNull
    public static String toPascalCase(@NotNull String pInput) {
        if (!pInput.contains("_") && !pInput.contains("-") && Character.isLowerCase(pInput.charAt(0))) {
            return pInput.substring(0, 1).toUpperCase() + pInput.substring(1);
        }

        if (pInput.contains("_")) {
            return convertUsingDelimiter(pInput, "_", false);
        }

        if (pInput.contains("-")) {
            return convertUsingDelimiter(pInput, "-", false);
        }

        BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.translate("notfound"));
        return pInput;
    }

    @NotNull
    public static String toSnakeCase(@NotNull String pInput) {
        String result = pInput.replaceAll("([a-z])([A-Z])", "$1_$2");
        result = result.toLowerCase();
        result = result.replace("-", "_");

        return result;
    }

    @NotNull
    public static String toKebabCase(@NotNull String pInput) {
        String result = pInput.replaceAll("([a-z])([A-Z])", "$1-$2");
        result = result.toLowerCase();
        result = result.replace("_", "-");

        return result;
    }

    @NotNull
    public static String toUpperSnakeCase(@NotNull String pInput) {
        String result = toSnakeCase(pInput);
        return result.toUpperCase();
    }

    @NotNull
    public static String toTrainCase(@NotNull String pInput) {
        String result = Objects.requireNonNull(toKebabCase(pInput)).replace("-", " ");
        return Objects.requireNonNull(toCamelCase(result)).replace(" ", "-");
    }

    @NotNull
    public static String toFlatcase(@NotNull String pInput) {
        return pInput.replaceAll("[_-]", "").toLowerCase();
    }

    @NotNull
    public static String toCobolCase(@NotNull String pInput) {
        String result = toKebabCase(pInput);
        return result.toUpperCase();
    }

    @NotNull
    private static String convertUsingDelimiter(@NotNull String pInput, @NotNull String pDelim, boolean pCamel) {
        String[] parts = pInput.split(pDelim);
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (pCamel && sb.isEmpty()) {
                sb.append(part.substring(0, 1).toLowerCase());
            } else {
                sb.append(part.substring(0, 1).toUpperCase());
            }
            sb.append(part.substring(1).toLowerCase());
        }

        return sb.toString();
    }
}
