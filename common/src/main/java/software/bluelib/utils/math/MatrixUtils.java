// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code class} providing utility methods for matrix operations.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #multiplyMatrices(double[][], double[][])} - Performs matrix multiplication on two matrices.</li>
 * <li>{@link #transposeMatrix(double[][])} - Computes the transpose of a matrix.</li>
 * <li>{@link #calculate2x2MatrixDeterminant(double[][])} - Calculates the determinant of a 2x2 matrix.</li>
 * <li>{@link #invert2x2Matrix(double[][])} - Calculates the inverse of a 2x2 matrix.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class MatrixUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private MatrixUtils() {}

    /**
     * A {@link Double}{@code [][]} that performs matrix multiplication on two matrices.
     *
     * @param pMatrixA {@link Double}{@code [][]} - The first matrix to be multiplied.
     * @param pMatrixB {@link Double}{@code [][]} - The second matrix to be multiplied.
     * @return The product of the two matrices.
     * @throws IllegalArgumentException if the number of columns in the first matrix does not match the number of rows in the second matrix.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double[][] multiplyMatrices(double[][] pMatrixA, double[][] pMatrixB) {
        int rowsA = pMatrixA.length;
        int colsA = pMatrixA[0].length;
        int colsB = pMatrixB[0].length;
        if (colsA != pMatrixB.length) {
            Throwable throwable = new IllegalArgumentException("Number of columns in the first matrix must be equal to the number of rows in the second matrix.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error performing matrix multiplication", throwable, true);
            return new double[0][0];
        }
        double[][] result = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += pMatrixA[i][k] * pMatrixB[k][j];
                }
            }
        }
        return result;
    }

    /**
     * A {@link Double}{@code [][]} that computes the transpose of a matrix.
     *
     * @param pMatrix {@link Double}{@code [][]} - The matrix to be transposed.
     * @return The transposed matrix.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double[][] transposeMatrix(double[][] pMatrix) {
        int rows = pMatrix.length;
        int cols = pMatrix[0].length;
        double[][] transposed = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = pMatrix[i][j];
            }
        }
        return transposed;
    }

    /**
     * A {@link Double}{@code [][]} that calculates the determinant of a 2x2 matrix.
     *
     * @param pMatrix {@link Double}{@code [][]} - The 2x2 matrix.
     * @return The determinant of the matrix.
     * @throws IllegalArgumentException if the matrix is not 2x2.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculate2x2MatrixDeterminant(double[][] pMatrix) {
        if (pMatrix.length != 2 || pMatrix[0].length != 2) {
            Throwable throwable = new IllegalArgumentException("Matrix must be 2x2.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating 2x2 matrix determinant", throwable, true);
            return Double.NaN;
        }
        return pMatrix[0][0] * pMatrix[1][1] - pMatrix[0][1] * pMatrix[1][0];
    }

    /**
     * A {@link Double}{@code [][]} that calculates the inverse of a 2x2 matrix.
     *
     * @param pMatrix {@link Double}{@code [][]} - The 2x2 matrix.
     * @return The inverse of the matrix.
     * @throws IllegalArgumentException if the matrix is not invertible or not 2x2.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double[][] invert2x2Matrix(double[][] pMatrix) {
        if (pMatrix.length != 2 || pMatrix[0].length != 2) {
            Throwable throwable = new IllegalArgumentException("Matrix must be 2x2.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error inverting 2x2 matrix", throwable, true);
            return new double[0][0];
        }
        double determinant = calculate2x2MatrixDeterminant(pMatrix);
        if (determinant == 0) {
            Throwable throwable = new IllegalArgumentException("Matrix is not invertible.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error inverting 2x2 matrix", throwable, true);
            return new double[0][0];
        }
        double[][] inverse = new double[2][2];
        inverse[0][0] = pMatrix[1][1] / determinant;
        inverse[0][1] = -pMatrix[0][1] / determinant;
        inverse[1][0] = -pMatrix[1][0] / determinant;
        inverse[1][1] = pMatrix[0][0] / determinant;
        return inverse;
    }
}
