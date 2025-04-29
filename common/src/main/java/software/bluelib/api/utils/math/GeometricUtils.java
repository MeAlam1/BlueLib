// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.math;

import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class GeometricUtils {

    private GeometricUtils() {}

    public static double calculateDistance2D(double pX1, double pY1, double pX2, double pY2) {
        double dx = pX2 - pX1;
        double dy = pY2 - pY1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double calculateDistance3D(double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2) {
        double dx = pX2 - pX1;
        double dy = pY2 - pY1;
        double dz = pZ2 - pZ1;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static double calculateCircleArea(double pRadius) {
        if (pRadius < 0) {
            Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating circle area", throwable, true);
            return Double.NaN;
        }
        return Math.PI * pRadius * pRadius;
    }

    public static double calculateCircleCircumference(double pRadius) {
        if (pRadius < 0) {
            Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating circle circumference", throwable, true);
            return Double.NaN;
        }
        return 2 * Math.PI * pRadius;
    }

    public static double calculateRectangleArea(double pWidth, double pHeight) {
        if (pWidth < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Width and height must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating rectangle area", throwable, true);
            return Double.NaN;
        }
        return pWidth * pHeight;
    }

    public static double calculateRectanglePerimeter(double pWidth, double pHeight) {
        if (pWidth < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Width and height must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating rectangle perimeter", throwable, true);
            return Double.NaN;
        }
        return 2 * (pWidth + pHeight);
    }

    public static double calculateTriangleArea(double pBase, double pHeight) {
        if (pBase < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Base and height must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating triangle area", throwable, true);
            return Double.NaN;
        }
        return 0.5 * pBase * pHeight;
    }

    public static double calculateTrianglePerimeter(double pSide1, double pSide2, double pSide3) {
        if (pSide1 < 0 || pSide2 < 0 || pSide3 < 0) {
            Throwable throwable = new IllegalArgumentException("Sides must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating triangle perimeter", throwable, true);
            return Double.NaN;
        }
        return pSide1 + pSide2 + pSide3;
    }

    public static double calculateSphereVolume(double pRadius) {
        if (pRadius < 0) {
            Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating sphere volume", throwable, true);
            return Double.NaN;
        }
        return (4.0 / 3.0) * Math.PI * Math.pow(pRadius, 3);
    }

    public static double calculateCubeSurfaceArea(double pSideLength) {
        if (pSideLength < 0) {
            Throwable throwable = new IllegalArgumentException("Side length must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating cube surface area", throwable, true);
            return Double.NaN;
        }
        return 6 * Math.pow(pSideLength, 2);
    }

    public static double calculateCylinderVolume(double pRadius, double pHeight) {
        if (pRadius < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Radius and height must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating cylinder volume", throwable, true);
            return Double.NaN;
        }
        return Math.PI * Math.pow(pRadius, 2) * pHeight;
    }

    public static double calculateConeSurfaceArea(double pRadius, double pSlantHeight) {
        if (pRadius < 0 || pSlantHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Radius and slant height must be non-negative.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating cone surface area", throwable, true);
            return Double.NaN;
        }
        return Math.PI * pRadius * (pRadius + pSlantHeight);
    }
}
