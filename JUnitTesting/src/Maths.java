import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

/*
 * A duplicate of the Maths Util class in the main project
 */
public class Maths 
{
	/*
	 * Sigmoid Function
	 */
    public static double sigmoid(final double value)
    {
        return (1.0 / (1.0 + Math.exp(-value)));
    }

	
    /*
     * Cross Entropy cost function
     */
    public static double costFunction(final double actual, final double target)
    {
    	return (target*Math.log(actual))+((1-target)*Math.log(1-actual));
    }

    /*
     * Gaussian activation function
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
     * Inverse of matrix
     */
    public static Matrix inverse (double [][] m)throws IllegalArgumentException
    {
    	Matrix matrixM = new Basic2DMatrix(m);
    	Matrix inverseM = matrixM.inverse(Matrices.DEFAULT_INVERTOR);
		return inverseM;	
    }
    
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

