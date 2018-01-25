import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
/**
 * This is the test cases for all JUnit Test
 * @author Chan Man Yi 1391904
 *
 */

public class RBFTest 
{
	private ArrayList<double[][]> trainingSet;
	private ArrayList<double[][]> kernalSet;
	private ArrayList<double[][]> testingSet;
	private double[] input;
	private double[] target;
	private double[] sample;
	private double[] hidden;
	
	private double variance ;
	
	private double[][] outputMatrix;
	
	private double[][] someMatrix;

	@Before
	public void setUp() throws Exception 
	{
		trainingSet = new ArrayList<double[][]>();
		trainingSet.add(new double[][]{{0.0, 0.0},{0.0}});
		trainingSet.add(new double[][]{{0.0, 1.0},{1.0}});
		trainingSet.add(new double[][]{{1.0, 0.0},{1.0}});
		trainingSet.add(new double[][]{{1.0, 1.0},{0.0}});
		
		kernalSet = new ArrayList<double[][]>();
		kernalSet.add(new double[][]{{0.0,0.0},{0.0}});
		kernalSet.add(new double[][]{{1.0,1.0},{0.0}});
		
		testingSet = new ArrayList<double[][]>();
		testingSet.add(new double [][] {{1.0, 0.0},{1.0}});
		testingSet.add(new double [][] {{0.0, 1.0},{1.0}});
	
	}
	
	@Test
	public void sigmoidFunctionTest()
	{
		Maths maths = new Maths();
		double sigmoid = maths.sigmoid(4);
		double expected = 0.982;
		assertEquals(expected, sigmoid, 0.01);
	}
	
	@Test
	public void costFunctionTest()
	{
		Maths maths = new Maths();
		double costFunction = maths.costFunction(0.5, 0.2);
		double expected = -0.693;
		assertEquals(costFunction, expected, 0.01);
	}
	
	@Test
	public void maxDistTest()
	{
		RBFNetwork r = new RBFNetwork();
		double maxDist = r.maxDistance(kernalSet);
		double expected = Math.sqrt(2);
		assertTrue(maxDist == expected);
	}
	
	@Test
	public void calVariancetest() 
	{
		RBFNetwork a = new RBFNetwork();
		double v = a.calculateVariance(kernalSet);
		double expected = (Math.sqrt(2)/2);
		assertEquals(v, expected, 0.01);
	}
	
	@Test
	public void indexTest()
	{
		double[] t = new double[]{0.0, 0.0, 0.0, 1, 0.0, 0.0, 0.0};
		RBFNetwork a = new RBFNetwork();
		int index = a.index(t);
		assertTrue(index==3);
	}
	
	
	@Test
	public void sampleToKernalDistTest()
	{
		double[] s = new double[]{0.0, 1.0};
		double v = Math.sqrt(2)/2;
		RBFNetwork a = new RBFNetwork();
		double[] outcome = a.sampleToKernalDist(s, kernalSet, v);
		double[] expected = new double[]{1.0,0.3678794411714424,0.3678794411714424};
		assertArrayEquals(outcome, expected, 0.001);
	}
	
	@Test
	public void transposeMatrixTest()
	{
		double[][] output = new double[][]{{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
		Maths m = new Maths();
		double[][] o = m.transposeMatrix(output);
		double[][] e = new double[][]{{1,4,7,10},{2,5,8,11},{3,6,9,12}};
		assertArrayEquals(o,e);
		
	}	

	
	@Test(expected = IllegalArgumentException.class)
	public void inverseMatrixCantPassTest()
	{
		Maths m = new Maths();
		double[][] a = new double[][]{{1,2,3},{4,5,6},{7,8,9}};
		Matrix o = m.inverse(a);
	}

	
	@Test
	public void matrixMul()
	{
		double[][] matrixA = new double[][]{{1,2},{3,4},{5,6}};
		double[][] matrixB = new double[][]{{1,2,3},{4,5,6}};
		double[][] matrixC = new double[][]{{9,12,15},{19,26,33},{29,40,51}};
		Maths m = new Maths();
		assertArrayEquals(m.matrixMul(matrixA, matrixB),matrixC);
	}

	@Test
	public void trainingTest()
	{
		RBFNetwork r = new RBFNetwork();
		r.train(trainingSet);
		r.test(testingSet);
	}
	
}
