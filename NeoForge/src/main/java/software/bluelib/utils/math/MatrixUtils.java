package software.bluelib.utils.math;

public class MatrixUtils {

    /**
     * A {@code double[][]} that performs matrix multiplication on two matrices.
     *
     * @param pMatrixA {@link double[][]} - The first matrix to be multiplied.
     * @param pMatrixB {@link double[][]} - The second matrix to be multiplied.
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
            throw new IllegalArgumentException("Number of columns in the first matrix must be equal to the number of rows in the second matrix.");
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
     * A {@code double[][]} that computes the transpose of a matrix.
     *
     * @param pMatrix {@link double[][]} - The matrix to be transposed.
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
     * A {@code double} that calculates the determinant of a 2x2 matrix.
     *
     * @param pMatrix {@link double[][]} - The 2x2 matrix.
     * @return The determinant of the matrix.
     * @throws IllegalArgumentException if the matrix is not 2x2.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double calculate2x2MatrixDeterminant(double[][] pMatrix) {
        if (pMatrix.length != 2 || pMatrix[0].length != 2) {
            throw new IllegalArgumentException("Matrix must be 2x2.");
        }
        return pMatrix[0][0] * pMatrix[1][1] - pMatrix[0][1] * pMatrix[1][0];
    }


    /**
     * A {@code double[][]} that calculates the inverse of a 2x2 matrix.
     *
     * @param pMatrix {@link double[][]} - The 2x2 matrix.
     * @return The inverse of the matrix.
     * @throws IllegalArgumentException if the matrix is not invertible or not 2x2.
     * @author MeAlam
     * @since 1.0.0
     */
    public static double[][] invert2x2Matrix(double[][] pMatrix) {
        if (pMatrix.length != 2 || pMatrix[0].length != 2) {
            throw new IllegalArgumentException("Matrix must be 2x2.");
        }
        double determinant = calculate2x2MatrixDeterminant(pMatrix);
        if (determinant == 0) {
            throw new IllegalArgumentException("Matrix is not invertible.");
        }
        double[][] inverse = new double[2][2];
        inverse[0][0] = pMatrix[1][1] / determinant;
        inverse[0][1] = -pMatrix[0][1] / determinant;
        inverse[1][0] = -pMatrix[1][0] / determinant;
        inverse[1][1] = pMatrix[0][0] / determinant;
        return inverse;
    }



}
