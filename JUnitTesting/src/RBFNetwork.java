import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.la4j.matrix.Matrix;

/**
 * This is a duplicate for the RBF network in the main project
 * @author Chan Man Yi 1391904
 *
 */

public class RBFNetwork 
{
	//initialise the number of neurons in input, hidden and output layer.
	private static int INPUT_NEURON_NUM = 1;
	private static int HIDDEN_NEURON_NUM;
	private static int OUTPUT_NEURON_NUM;
	
	private static ArrayList<double[][]>kernalSet;
	private static double kernalProportion = 0.5;
	private static double input[];
	private static double hidden[]; //after performing gaussian, each input
	private static double target[];
	private static double variance;
	private static int numberOfSamples;
	
	private static double weightHO[][];
	private static double hiddenMatrix[][]; //contain all hidden[]
	private static double outputMatrix[][];
	private static double weight[][];
	private static double accuracy;
	
	/*
	 * Training Function
	 * @param trainingSet - the training set
	 */
	public void train(ArrayList<double[][]> trainingSet)
	{
		kernalSet = new ArrayList<double[][]>();
		kernalSet=selectKernalSet(trainingSet, kernalProportion);
		System.out.println("Selected Kernal set: "+ Arrays.deepToString(kernalSet.toArray()));
			
		//find width of gaussian function, ie variance
		variance = calculateVariance(kernalSet);
		System.out.println("Variance: "+variance);
		
		input = new double [trainingSet.get(0)[0].length];
		target = new double [trainingSet.get(0)[1].length];
		hidden = new double [kernalSet.size()+1];
		hiddenMatrix = new double [trainingSet.size()][hidden.length];
		outputMatrix = new double [trainingSet.size()][1];
		weight = new double[kernalSet.size()+1][1];
		
		//for each training item
		for (int t=0;t<trainingSet.size();t++)
		{
			//extract corresponding input
			for(int i=0; i<input.length;i++)
			{
				input[i]=trainingSet.get(t)[0][i];
			}
			System.out.println("Training Input "+t+": "+ Arrays.toString(input));
			
			//extract corresponding target output
			for(int i=0; i<target.length;i++)
			{
				target[i]=(int) trainingSet.get(t)[1][i];
			}
			System.out.println("Desire output "+t+": "+ Arrays.toString(target));

			//convert the target output in the dataset into a single number representation.
			int in = index(target);
			System.out.println(in);
			//outputMatrix[t][0]=in;
			
			//for XOR only
				outputMatrix[t][0]=trainingSet.get(t)[1][0];

			
			//calculate distance between this training item with the kernal set,
			//together with the gaussian function, stored in hidden[]
			double[] a= sampleToKernalDist(input, kernalSet, variance);
			System.out.println("Sample "+t+" to kernal distance: "+Arrays.toString(a));
			//build up the matrix to perform pseudo inverse later
			hiddenMatrix[t]=a;			
		}
		System.out.println("All hidden matrices combined: "+Arrays.deepToString(hiddenMatrix));
		
		//find weights by performing pseudo inverse
		 double[][] pseudo = pseudoInverse(hiddenMatrix);
		 
		 System.out.println("Output Matrix: "+Arrays.deepToString(outputMatrix));
		 weight = calculateWeight(pseudo);
		 
		 System.out.println("Calculated weights: "+ Arrays.deepToString(weight));
		
	}
	
	/*
	 * Testing the network
	 * @param trainingSet - the training set
	 */
	public void test(ArrayList<double[][]> testingSet)
	{
		input = new double [testingSet.get(0)[0].length];
		target = new double [testingSet.get(0)[1].length];
		hiddenMatrix = new double [testingSet.size()][hidden.length];
		int counter=0;

		//for each testing set
		for(int t=0;t<testingSet.size();t++)
		{
			//extract corresponding input
			for(int i=0; i<input.length;i++)
			{
				input[i]=testingSet.get(t)[0][i];
			}
			System.out.println("Testing Input "+t+": "+ Arrays.toString(input));
			
			//extract corresponding target output
			for(int i=0; i<target.length;i++)
			{
				target[i]=(int) testingSet.get(t)[1][i];
			}
			System.out.println("TestingSet Desire output "+t+": "+ Arrays.toString(target));
			
			//convert the target output in the dataset into a single number representation.
			int in = index(target);
			System.out.println(in);
			
			//calculate distance between this training item with the kernal set,
			//together with the gaussian function, stored in hidden[]
			double[] a= sampleToKernalDist(input, kernalSet, variance);
			System.out.println("Sample "+t+" to kernal distance: "+Arrays.toString(a));
			//build up the matrix to perform pseudo inverse later
			hiddenMatrix[t]=a;	
			
			//for each hidden neuron, calculate the weighted sum 
			double weightedSum = calculateWeightSum(a);
			System.out.println("Weighted Sum of testing input"+t+": "+weightedSum);
			
			//check if this weightedSum equals to the target output
			//add up all correctly identified samples.
			
			if(weightedSum == outputMatrix[t][0])
			{
				counter++;
			}

		}
		
		//calculate the percantage of correctly identified testing samples.
		accuracy = (counter/testingSet.size())*100;
		System.out.println("Network is "+accuracy+"% correct");
	}
	
	/* 
	 * Calculate the weighted sum
	 * @param sampleToKernalDist - the ditance between the input and th kernal set.
	 * @return weightedSumHO - the weighted sum between hidden neuron and output neuron.
	 */
	private double calculateWeightSum(double[]sampleToKernalDist) 
	{
		double weightedSumHO;
		System.out.println("check sample to kernal dist: "+Arrays.toString(sampleToKernalDist));
		System.out.println("check weight: "+ Arrays.deepToString(weight));
		
		//calculate the output value
		weightedSumHO=0.0;
		for(int h=0;h<sampleToKernalDist.length;h++)
		{
			//weightedSumHO += hidden[h]*weight[h][0];
			weightedSumHO += sampleToKernalDist[h]*weight[h][0];
		}
		System.out.println("weight before round: "+weightedSumHO);
		System.out.println("weight after round: "+(int) Math.round(weightedSumHO));
		return Math.round(weightedSumHO);		
	}


	/*
	 * Calculate weights
	 */
	private double[][] calculateWeight(double[][] pseudo) 
	{
		return Maths.matrixMul(pseudo, outputMatrix);
	}


	public double[][] pseudoInverse(double[][] matrix) 
	{
		//transpose of matrix
		double[][] transpose=Maths.transposeMatrix(matrix);
		//System.out.println(Arrays.deepToString(transpose));
		
		double[][] transposeAndItself = (Maths.matrixMul(transpose, matrix));
		//System.out.println(Arrays.deepToString(transposeAndItself));
		
		//perform ( A^T * A )^-1
		//Matrix pseudo = Maths.inverse(transposeAndItself);
		double [][]pseudo = Maths.invert(transposeAndItself);

		//multiply this matrix by the transpose 
		//perform ( A^T * A )^-1 *A^T
		double[][] pseudoInverse = Maths.matrixMul(pseudo, transpose);
		System.out.println("A+ ="+Arrays.deepToString(pseudoInverse));
		return pseudoInverse;
	}


	/*
	 * finding the maximum index in the output array
	 */
	public int index(double[] targetOutput) {
		int in=0;
		double max = targetOutput[in];
		for(int i=0;i<targetOutput.length;i++)
		{
			if(targetOutput[i]>max)
			{
				max = targetOutput[i];
				in =i;
			}
		}
		return in;
		
	}
	
	/*
	 * Convert Strings to chara
	 */
	public String indexToChara (int index)
	{
		String[] s = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		
		return s[index];
	}

	/*
	 * calculate the variance
	 */
	public double calculateVariance(ArrayList<double[][]> kernalSet) 
	{
		double variance = maxDistance(kernalSet)/Math.sqrt(2*numberOfSamples);
		return variance;
	}


	/*
	 * This method selects the kernal set randomly from the training set
	 * @param trainingSet - imported training set
	 * @param kernalProportion - amount of samples want from the training set
	 * @return kernalSet - return the whole sample set
	 */
	public ArrayList<double[][]> selectKernalSet(ArrayList<double[][]> trainingSet, double kernalProportion)
	{
		//number of samples taken from the training set
		numberOfSamples = (int) (trainingSet.size()*kernalProportion);
		
		//assign that number of samples randomly to the kernal set
		for(int i=0;i<numberOfSamples;i++)
		{
			int min = 0;
			int max = trainingSet.size()-1;
			Random r = new Random();
			int random = r.nextInt(max - min + 1) + min;
			System.out.println("Random Number: "+random);
			System.out.println("kernal selected: "+Arrays.deepToString(trainingSet.get(random)));
			System.out.println("kernal set right now: "+Arrays.deepToString(kernalSet.toArray()));
			kernalSet.add(trainingSet.get(random));
		}
		
		return kernalSet;
	}
	
	/*
	 * This method calculates the maximum distance between 2 samples in the kernal set, this will become part of the width of the gaussian function. 
	 * @param kernalSet - samples used in the hidden layer
	 * @return max - the maximum distance
	 */
	public double maxDistance (ArrayList<double[][]> kernalSet)
	{
		double max = 0;
		
		for(int x=0;x<kernalSet.size();x++)
		{
			for(int y=0;y<kernalSet.size();y++)
			{
				double distance = 0;
				for(int length=0;length<(kernalSet.get(0)[0].length);length++)
				{
					distance += ((kernalSet.get(x)[0][length])-(kernalSet.get(y)[0][length]))*((kernalSet.get(x)[0][length])-(kernalSet.get(y)[0][length]));
				}
				double squareRootDist = Math.sqrt(distance);
				if(squareRootDist>max)
				{
					max = squareRootDist;
				}
			}
		}
		System.out.println("max distance between kernals: "+ max);

		return max;
	}
	
	/*
	 * This method calculates the distance between the sample input and the kernal set
	 * @param sample - each iuput sample
	 * @param kernalSet -  each hidden neuron
	 * @param variance - the width of gaussian function
	 * @return g - distance together with the gaussian function
	 */
	public double[] sampleToKernalDist (double[] sample, ArrayList<double[][]> kernalSet, double variance)
	{
		double distance=0.0;
		double g=0.0;
		hidden = new double [kernalSet.size()+1]; //for testing purpose

		hidden[0]=1;
		for(int i=0;i<kernalSet.size();i++)
		{
			distance = 0.0;
			for(int y=0; y<kernalSet.get(0)[0].length;y++)
			{
				distance += (sample[y]-kernalSet.get(i)[0][y])*(sample[y]-kernalSet.get(i)[0][y]);
			}
			double squareRootDist = Math.sqrt(distance);
			g = Maths.gaussianRBF(squareRootDist, variance);
			hidden[i+1]= g;
		}
		return hidden;
		
	}
	
}
