/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.math;

import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public class GeometricUtils {

	private GeometricUtils() {}

	public static @NotNull Double calculateDistance2D(@NotNull Double pX1, @NotNull Double pY1, @NotNull Double pX2, @NotNull Double pY2) {
		Double dx = pX2 - pX1;
		Double dy = pY2 - pY1;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static @NotNull Double calculateDistance3D(@NotNull Double pX1, @NotNull Double pY1, @NotNull Double pZ1, @NotNull Double pX2, @NotNull Double pY2, @NotNull Double pZ2) {
		Double dx = pX2 - pX1;
		Double dy = pY2 - pY1;
		Double dz = pZ2 - pZ1;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static @NotNull Double calculateCircleArea(@NotNull Double pRadius) {
		if (pRadius < 0) {
			Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "circle area"), throwable);
			return Double.NaN;
		}
		return Math.PI * pRadius * pRadius;
	}

	public static @NotNull Double calculateCircleCircumference(@NotNull Double pRadius) {
		if (pRadius < 0) {
			Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "circle circumference"), throwable);
			return Double.NaN;
		}
		return 2 * Math.PI * pRadius;
	}

	public static @NotNull Double calculateRectangleArea(@NotNull Double pWidth, @NotNull Double pHeight) {
		if (pWidth < 0 || pHeight < 0) {
			Throwable throwable = new IllegalArgumentException("Width and height must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "rectangle area"), throwable);
			return Double.NaN;
		}
		return pWidth * pHeight;
	}

	public static @NotNull Double calculateRectanglePerimeter(@NotNull Double pWidth, @NotNull Double pHeight) {
		if (pWidth < 0 || pHeight < 0) {
			Throwable throwable = new IllegalArgumentException("Width and height must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "rectangle perimeter"), throwable);
			return Double.NaN;
		}
		return 2 * (pWidth + pHeight);
	}

	public static @NotNull Double calculateTriangleArea(@NotNull Double pBase, @NotNull Double pHeight) {
		if (pBase < 0 || pHeight < 0) {
			Throwable throwable = new IllegalArgumentException("Base and height must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "triangle area"), throwable);
			return Double.NaN;
		}
		return 0.5 * pBase * pHeight;
	}

	public static @NotNull Double calculateTrianglePerimeter(@NotNull Double pSide1, @NotNull Double pSide2, @NotNull Double pSide3) {
		if (pSide1 < 0 || pSide2 < 0 || pSide3 < 0) {
			Throwable throwable = new IllegalArgumentException("Sides must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "triangle perimeter"), throwable);
			return Double.NaN;
		}
		return pSide1 + pSide2 + pSide3;
	}

	public static @NotNull Double calculateSphereVolume(@NotNull Double pRadius) {
		if (pRadius < 0) {
			Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "sphere volume"), throwable);
			return Double.NaN;
		}
		return (4.0 / 3.0) * Math.PI * Math.pow(pRadius, 3);
	}

	public static @NotNull Double calculateCubeSurfaceArea(@NotNull Double pSideLength) {
		if (pSideLength < 0) {
			Throwable throwable = new IllegalArgumentException("Side length must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "cube surface area"), throwable);
			return Double.NaN;
		}
		return 6 * Math.pow(pSideLength, 2);
	}

	public static @NotNull Double calculateCylinderVolume(@NotNull Double pRadius, @NotNull Double pHeight) {
		if (pRadius < 0 || pHeight < 0) {
			Throwable throwable = new IllegalArgumentException("Radius and height must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "cylinder volume"), throwable);
			return Double.NaN;
		}
		return Math.PI * Math.pow(pRadius, 2) * pHeight;
	}

	public static @NotNull Double calculateConeSurfaceArea(@NotNull Double pRadius, @NotNull Double pSlantHeight) {
		if (pRadius < 0 || pSlantHeight < 0) {
			Throwable throwable = new IllegalArgumentException("Radius and slant height must be non-negative.");
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.calc", "cone surface area"), throwable);
			return Double.NaN;
		}
		return Math.PI * pRadius * (pRadius + pSlantHeight);
	}
}
