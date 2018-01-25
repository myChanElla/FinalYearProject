package utils;

import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
/**
 * This is a utility class containing all required mathematical equations.
 * @author Chan Man Yi 1391904
 *
 */
public class MathsUtils 
{
	/*
	 * Sigmoid Function
	 */
    public static double sigmoid(final double value)
    {
        return (1.0 / (1.0 + Math.exp(-value)));
    }

    /*
     * Derivative of Sigmoid function
     */
    public static double sigmoidDerivative(final double value)
    {
        return (value * (1.0 - value));
    }
    
    /*
     * Cross Entropy cost function
     */
    public static double costFunction(final double actual, final double target)
    {
    	return (target*Math.log(actual))+((1-target)*Math.log(1-actual));
    }
    
    /*
     * Gaussian function
     */
    public static double gaussianRBF(final double value, double variance)
    {
    	return Math.exp(-((value*value)/(2*(variance*variance))));
    }
    
    public static double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
    
    /*
     * Transpose of a matrix
     */
    public static Matrix inverse (double [][] m)//throws IllegalArgumentException
    {
    	System.out.println("inverse method entered");
    	Matrix matrixM = new Basic2DMatrix(m);
    	Matrix inverseM = matrixM.inverse(Matrices.DEFAULT_INVERTOR);
		return inverseM;
    }
    
    /*
     * Inverse of a matrix
     */
    public static double[][] invert(double a[][]) 
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) 
            b[i][i] = 1;
 
        // Transform the matrix into an upper triangle
        gaussian(a, index);
 
        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                    	    -= a[index[j]][i]*b[index[i]][k];
 
        // Perform backward substitutions
        for (int i=0; i<n; ++i) 
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k) 
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }
    
    // Method to carry out the partial-pivoting Gaussian
    // elimination.  Here index[] stores pivoting order.
     public static void gaussian(double a[][], int index[]) 
     {
         int n = index.length;
         double c[] = new double[n];
  
         // Initialize the index
         for (int i=0; i<n; ++i) 
             index[i] = i;
  
         // Find the rescaling factors, one from each row
         for (int i=0; i<n; ++i) 
         {
             double c1 = 0;
             for (int j=0; j<n; ++j) 
             {
                 double c0 = Math.abs(a[i][j]);
                 if (c0 > c1) c1 = c0;
             }
             c[i] = c1;
         }
  
         // Search the pivoting element from each column
         int k = 0;
         for (int j=0; j<n-1; ++j) 
         {
             double pi1 = 0;
             for (int i=j; i<n; ++i) 
             {
                 double pi0 = Math.abs(a[index[i]][j]);
                 pi0 /= c[index[i]];
                 if (pi0 > pi1) 
                 {
                     pi1 = pi0;
                     k = i;
                 }
             }
  
             // Interchange rows according to the pivoting order
             int itmp = index[j];
             index[j] = index[k];
             index[k] = itmp;
             for (int i=j+1; i<n; ++i) 	
             {
                 double pj = a[index[i]][j]/a[index[j]][j];
  
                 // Record pivoting ratios below the diagonal
                 a[index[i]][j] = pj;
  
                 // Modify other elements accordingly
                 for (int l=j+1; l<n; ++l)
                     a[index[i]][l] -= pj*a[index[j]][l];
             }
         }
     }
    
     /*
      * Determinant of a matrix
      */
    public static double findDeterminant(double[][] A, int N)
    {
        double det=0;
        if(N == 1)
        {
            det = A[0][0];
        }
        else if (N == 2)
        {
            det = A[0][0]*A[1][1] - A[1][0]*A[0][1];
        }
        else
        {
            det=0;
            for(int j1=0;j1<N;j1++)
            {
                double[][] m = new double[N-1][];
                for(int k=0;k<(N-1);k++)
                {
                    m[k] = new double[N-1];
                }
                for(int i=1;i<N;i++)
                {
                    int j2=0;
                    for(int j=0;j<N;j++)
                    {
                        if(j == j1)
                            continue;
                        m[i-1][j2] = A[i][j];
                        j2++;
                    }
                }
                det += Math.pow(-1.0,1.0+j1+1.0)* A[0][j1] * findDeterminant(m,N-1);
            }
        }
        return det;
    }
     
    /*
     * Matrix multiplication
     */
    public static double[][] matrixMul(double[][] A, double[][] B) 
    {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] C = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C; 
}
}
