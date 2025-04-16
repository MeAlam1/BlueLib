// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.math;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class MatrixUtils {

    private MatrixUtils() {}

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

    public static double calculate2x2MatrixDeterminant(double[][] pMatrix) {
        if (pMatrix.length != 2 || pMatrix[0].length != 2) {
            Throwable throwable = new IllegalArgumentException("Matrix must be 2x2.");
            BaseLogger.log(BaseLogLevel.ERROR, "Error calculating 2x2 matrix determinant", throwable, true);
            return Double.NaN;
        }
        return pMatrix[0][0] * pMatrix[1][1] - pMatrix[0][1] * pMatrix[1][0];
    }

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
