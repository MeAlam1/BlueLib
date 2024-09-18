// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogger;

public class GeometricUtils {

    /**
     * A {@code double} that calculates the Euclidean distance between two points in 2D space.
     *
     * @param pX1 {@link double} - The x-coordinate of the first point.
     * @param pY1 {@link double} - The y-coordinate of the first point.
     * @param pX2 {@link double} - The x-coordinate of the second point.
     * @param pY2 {@link double} - The y-coordinate of the second point.
     * @return The distance between the two points.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateDistance2D(double pX1, double pY1, double pX2, double pY2) {
        double dx = pX2 - pX1;
        double dy = pY2 - pY1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * A {@code double} that calculates the Euclidean distance between two points in 3D space.
     *
     * @param pX1 {@link double} - The x-coordinate of the first point.
     * @param pY1 {@link double} - The y-coordinate of the first point.
     * @param pZ1 {@link double} - The z-coordinate of the first point.
     * @param pX2 {@link double} - The x-coordinate of the second point.
     * @param pY2 {@link double} - The y-coordinate of the second point.
     * @param pZ2 {@link double} - The z-coordinate of the second point.
     * @return The distance between the two points in 3D space.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateDistance3D(double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2) {
        double dx = pX2 - pX1;
        double dy = pY2 - pY1;
        double dz = pZ2 - pZ1;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * A {@code double} that calculates the area of a circle given its radius.
     *
     * @param pRadius {@link double} - The radius of the circle.
     * @return The area of the circle.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateCircleArea(double pRadius) {
        if (pRadius < 0) {
            Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
            BaseLogger.logError("Error calculating circle area", throwable);
            return Double.NaN;
        }
        return Math.PI * pRadius * pRadius;
    }

    /**
     * A {@code double} that calculates the circumference of a circle given its radius.
     *
     * @param pRadius {@link double} - The radius of the circle.
     * @return The circumference of the circle.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateCircleCircumference(double pRadius) {
        if (pRadius < 0) {
            Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
            BaseLogger.logError("Error calculating circle circumference", throwable);
            return Double.NaN;
        }
        return 2 * Math.PI * pRadius;
    }

    /**
     * A {@code double} that calculates the area of a rectangle given its width and height.
     *
     * @param pWidth {@link double} - The width of the rectangle.
     * @param pHeight {@link double} - The height of the rectangle.
     * @return The area of the rectangle.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateRectangleArea(double pWidth, double pHeight) {
        if (pWidth < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Width and height must be non-negative.");
            BaseLogger.logError("Error calculating rectangle area", throwable);
            return Double.NaN;
        }
        return pWidth * pHeight;
    }

    /**
     * A {@code double} that calculates the perimeter of a rectangle given its width and height.
     *
     * @param pWidth {@link double} - The width of the rectangle.
     * @param pHeight {@link double} - The height of the rectangle.
     * @return The perimeter of the rectangle.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateRectanglePerimeter(double pWidth, double pHeight) {
        if (pWidth < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Width and height must be non-negative.");
            BaseLogger.logError("Error calculating rectangle perimeter", throwable);
            return Double.NaN;
        }
        return 2 * (pWidth + pHeight);
    }

    /**
     * A {@code double} that calculates the area of a triangle given its base and height.
     *
     * @param pBase {@link double} - The base of the triangle.
     * @param pHeight {@link double} - The height of the triangle.
     * @return The area of the triangle.
     * @since 1.0.0
     */
    public static double calculateTriangleArea(double pBase, double pHeight) {
        if (pBase < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Base and height must be non-negative.");
            BaseLogger.logError("Error calculating triangle area", throwable);
            return Double.NaN;
        }
        return 0.5 * pBase * pHeight;
    }

    /**
     * A {@code double} that calculates the perimeter of a triangle given its three sides.
     *
     * @param pSide1 {@link double} - The first side of the triangle.
     * @param pSide2 {@link double} - The second side of the triangle.
     * @param pSide3 {@link double} - The third side of the triangle.
     * @return The perimeter of the triangle.
     * @since 1.0.0
     */
    public static double calculateTrianglePerimeter(double pSide1, double pSide2, double pSide3) {
        if (pSide1 < 0 || pSide2 < 0 || pSide3 < 0) {
            Throwable throwable = new IllegalArgumentException("Sides must be non-negative.");
            BaseLogger.logError("Error calculating triangle perimeter", throwable);
            return Double.NaN;
        }
        return pSide1 + pSide2 + pSide3;
    }

    /**
     * A {@code double} that calculates the volume of a sphere given its radius.
     *
     * @param pRadius {@link double} - The radius of the sphere.
     * @return The volume of the sphere.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateSphereVolume(double pRadius) {
        if (pRadius < 0) {
            Throwable throwable = new IllegalArgumentException("Radius must be non-negative.");
            BaseLogger.logError("Error calculating sphere volume", throwable);
            return Double.NaN;
        }
        return (4.0 / 3.0) * Math.PI * Math.pow(pRadius, 3);
    }

    /**
     * A {@code double} that calculates the surface area of a cube given its side length.
     *
     * @param pSideLength {@link double} - The length of a side of the cube.
     * @return The surface area of the cube.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateCubeSurfaceArea(double pSideLength) {
        if (pSideLength < 0) {
            Throwable throwable = new IllegalArgumentException("Side length must be non-negative.");
            BaseLogger.logError("Error calculating cube surface area", throwable);
            return Double.NaN;
        }
        return 6 * Math.pow(pSideLength, 2);
    }

    /**
     * A {@code double} that calculates the volume of a cylinder given its radius and height.
     *
     * @param pRadius {@link double} - The radius of the cylinder's base.
     * @param pHeight {@link double} - The height of the cylinder.
     * @return The volume of the cylinder.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateCylinderVolume(double pRadius, double pHeight) {
        if (pRadius < 0 || pHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Radius and height must be non-negative.");
            BaseLogger.logError("Error calculating cylinder volume", throwable);
            return Double.NaN;
        }
        return Math.PI * Math.pow(pRadius, 2) * pHeight;
    }

    /**
     * A {@code double} that calculates the surface area of a cone given its radius and slant height.
     *
     * @param pRadius {@link double} - The radius of the base of the cone.
     * @param pSlantHeight {@link double} - The slant height of the cone.
     * @return The surface area of the cone.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculateConeSurfaceArea(double pRadius, double pSlantHeight) {
        if (pRadius < 0 || pSlantHeight < 0) {
            Throwable throwable = new IllegalArgumentException("Radius and slant height must be non-negative.");
            BaseLogger.logError("Error calculating cone surface area", throwable);
            return Double.NaN;
        }
        return Math.PI * pRadius * (pRadius + pSlantHeight);
    }
}
