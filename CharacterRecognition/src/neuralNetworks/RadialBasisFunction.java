package neuralNetworks;

import guiPanels.DrawLineGraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.la4j.matrix.Matrix;

import utils.MathsUtils;
import data.ReadData;

/**
 * This class illustrate the implementation of the Radial Basis Function
 * @author Chan Man Yi 1391904
 *
 */
public class RadialBasisFunction 
{
	private static int OUTPUT_NEURON_NUM=52;
	
	private static ArrayList<double[][]>kernalSet;
	private static double kernalProportion;
	private static double input[];
	private static double hidden[]; //after performing gaussian, each input
	private static double target[];
	private static double actual[];
	private static double variance;
	private static int numberOfSamples;
	
	private static double weightHO[][];
	private static double hiddenMatrix[][]; //contain all hidden[]
	private static double outputMatrix[][];
	private static double weight[][];
	private static double accuracy;
	private static int epoch;
	private static double meanSquaredError;
	
	/*
	 * This method trains the network
	 * @param trainingSet - the set of training inputs
	 */
	public static void train(ArrayList<double[][]> trainingSet) throws IOException
	{
		kernalSet = new ArrayList<double[][]>();
		
		//select the kernal set
		kernalSet=selectKernalSet(trainingSet, kernalProportion);
		kernalSetPercentage();
		
		//Turn kernal set into binary representation.
		PrintWriter pw = new PrintWriter("KernalSet.txt");
		for(int i=0;i<kernalSet.size();i++)
		{
			for(int x=0;x<(kernalSet.get(i)[0].length);x++)
			{
				pw.print(kernalSet.get(i)[0][x]);
				pw.print(" ");
			}
			for(int y=0;y<(kernalSet.get(i)[1].length);y++)
			{
				pw.print((int)kernalSet.get(i)[1][y]);
				pw.print(" ");
			}
			pw.println();
		}
		pw.close();
		
		//find width of gaussian function, ie variance
		variance = calculateVariance(kernalSet);
		System.out.println("Variance: "+variance);
		
		input = new double [trainingSet.get(0)[0].length];
		target = new double [trainingSet.get(0)[1].length];
		actual = new double [trainingSet.get(0)[1].length];
		hidden = new double [kernalSet.size()];
		hiddenMatrix = new double [trainingSet.size()][hidden.length];
		outputMatrix = new double [trainingSet.size()][1];
		weight = new double[kernalSet.size()+1][1];
		
		//assign random initial weights to weightHO[][]
		assignRandomWeights();
		System.out.println("initial weight:"+Arrays.toString(weightHO[0]));
		int count =0;
		int maxCount = trainingSet.size();
		
		//for each training item
		for (int t=0;t<epoch;t++)
		{
			if(count == maxCount)
			{
				count = 0;
				double totalError =totalError();
				//updateGraph(t, totalError);
				//System.out.println("total error after all batch learning: "+totalError);
			}

			
			//extract corresponding input
			for(int i=0; i<input.length;i++)
			{
				input[i]=trainingSet.get(count)[0][i];
			}
			
			//extract corresponding target output
			for(int i=0; i<target.length;i++)
			{
				target[i]=trainingSet.get(count)[1][i];
			}
			
			//repeat again when epoch is more than training set

			
			//calculate distance between this training item with the kernal set,
			double[] a= sampleToKernalDist(input, kernalSet, variance);
			
			//calculate the weighted sum for each random weights to produce an actual output
			calculateWeightedSum(a);
			
			//update the weight
			backPropagate(); 
			count +=1;
		}
		System.out.println("weights after update:"+Arrays.toString(weightHO[0]));		
	}
	
	/*
	 * This method calculates the error of a cost function
	 */
	private static double totalError() 
	{
		meanSquaredError=0.0;
		double totalError=0.0;
		for(int out=0;out<OUTPUT_NEURON_NUM;out++)
		{
			totalError += Math.pow(target[out]-actual[out], 2);
		}
		meanSquaredError=totalError/52.0;
		return meanSquaredError;
		
	}

	/*
	 * This method update the line graph during training
	 * @param e - epoch
	 * @param totalError- the erroe
	 */
	private static void updateGraph(int e, double totalError) 
	{
		DrawLineGraph.update(e, totalError);
	}


	/*
	 * This method update the weights by back propagation
	 */
    public static void backPropagate() 
    {
    	double[]outputError=new double[OUTPUT_NEURON_NUM];
		
    	//(step 1) calculate error in output layer
    	for(int x=0;x<OUTPUT_NEURON_NUM;x++)
    	{
    		outputError[x]=target[x]-actual[x];
    	}
    	
    	//(Step 2) Update weight in output layer
    	for(int x=0;x<OUTPUT_NEURON_NUM;x++)
    	{
    		for(int h=0;h<kernalSet.size();h++)
    		{
    			weightHO[h][x]+=(0.2*outputError[x]*hidden[h]);
    		}
    	}
    	return;
	}

    /*
     * This method calculates the percentage of each characters selected as the kernal set
     */
    public static void kernalSetPercentage()
    {
    	int counter = 0;
    	for(int i=0;i<OUTPUT_NEURON_NUM;i++)
    	{
    		counter=0;
    		for(int k=0;k<kernalSet.size();k++)
    		{
    			//System.out.println("Index currently looking at: "+index(kernalSet.get(k)[1]));
    			if(index(kernalSet.get(k)[1])==i)
    			{
    				counter++;
    			}
    		}
    		double percentage = ((double)counter/(double)kernalSet.size())*100;
    		
    		System.out.println("Proportion of "+indexToChara(i)+" in the kernal set is "+percentage+" %");
    	}
    	
    }

    /*
     * This method is to test the network with unseen data
     * @param testing - the testing data
     */
	public static void test(ArrayList<double[][]> testingSet)
	{
		hiddenMatrix = new double [testingSet.size()][hidden.length];
		double trace=0;
		double error =0;
		for(int t=0;t<testingSet.size();t++)
		{
			//extract corresponding input
			for(int i=0; i<input.length;i++)
			{
				input[i]=testingSet.get(t)[0][i];
			}
			
			//extract corresponding target output
			for(int i=0; i<target.length;i++)
			{
				target[i]=testingSet.get(t)[1][i];
			}
			
			//calculate distance between this training item with the kernal set,
			double[] a= sampleToKernalDist(input, kernalSet, variance);
			calculateWeightedSum(a);

			//compare actual output with target output
			if(index(actual)==index(target))
			{
				trace +=1;
			}
			else
			{
				System.out.println("Target input "+ indexToChara(index(target))+" was mistaken by "+ indexToChara(index(actual)));
			}
			
			error += totalError();
		}
		
		//calculate the percantage of correctly identified testing samples.
		accuracy=trace / testingSet.size() * 100.0;
        System.out.println("Network is " + accuracy + "% Correct");
	}

	/*
	 * This method calculates the weighted sum
	 * @param a - output neuron
	 */
	public static void calculateWeightedSum(double[] a) 
	{
		double weightedSumHO;
		for(int o=0;o<OUTPUT_NEURON_NUM;o++)
		{
			weightedSumHO=0.0;
			for(int h=0;h<(a.length);h++)
			{
				weightedSumHO += (a[h]*weightHO[h][o]);
			}
			actual[o]=weightedSumHO;
		}
		return;
	}

	/*
	 * This method find out which output neuron will fire, ie the index which has the maximum number
	 * @param target output- the array of target output
	 * @return in - the index
	 */
	private static int index(double[] targetOutput) 
	{
		int in=0;
		double max = targetOutput[in];
		for(int i=0;i<OUTPUT_NEURON_NUM;i++)
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
	 * This method assigns random weights from the hidden neuron to the output neurons
	 */
	private static void assignRandomWeights() {
		weightHO = new double [hidden.length][target.length];
		
		for(int i=0;i<hidden.length; i++)
		{
			for(int j=0;j<target.length; j++)
			{
				double min = -0.2;
				double max = 0.2;
				Random r = new Random();
				double random = min + (max - min) * r.nextDouble();
				weightHO[i][j]=random;
			}
		}
		return;
	}
	
	/*
	 * This method converts the index of the output back into english characters
	 * @param index- the index of output neuron
	 * @return s[] - value stored in the index
	 */
	public static String indexToChara (int index)
	{
		String[] s = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		
		return s[index];
	}

	/*
	 * This method calculates the variance
	 * @param kernalSet - set of hidden neurons
	 * @return variance - variance
	 */
	private static double calculateVariance(ArrayList<double[][]> kernalSet) 
	{
		double variance = maxDistance(kernalSet)/Math.sqrt(2*numberOfSamples);
		return variance;
//		double variance = averageDistance(kernalSet);
//		return variance;

	}


	/*
	 * This method selects the kernal set randomly from the training set
	 * @param trainingSet - imported training set
	 * @param kernalProportion - amount of samples want from the training set
	 * @return kernalSet - return the whole sample set
	 */
	public static ArrayList<double[][]> selectKernalSet(ArrayList<double[][]> trainingSet, double kernalProportion)
	{
		//number of samples taken from the training set
		numberOfSamples = (int) (trainingSet.size()*kernalProportion);
		System.out.println("Number of Samples: "+numberOfSamples);
		
		//assign that number of samples randomly to the kernal set
		for(int i=0;i<numberOfSamples;i++)
		{
			int min = 0;
			int max = trainingSet.size()-1;
			Random r = new Random();
			int random = r.nextInt(max - min + 1) + min;
			kernalSet.add(trainingSet.get(random));
		}
		System.out.println("size of kernal set: "+ kernalSet.size());
		return kernalSet;
	}
	
	/*
	 * This method calculates the maximum distance between 2 samples in the kernal set, this will become part of the width of the gaussian function. 
	 * @param kernalSet - samples used in the hidden layer
	 * @return max - the maximum distance
	 */
	public static double maxDistance (ArrayList<double[][]> kernalSet)
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
	 * This method calculates the maximum distance between 2 samples in the kernal set, this will become part of the width of the gaussian function. 
	 * @param kernalSet - samples used in the hidden layer
	 * @return max - the maximum distance
	 */
	public static double averageDistance (ArrayList<double[][]> kernalSet)
	{
		double max = 0;
		double totalDistance =0;
		double averageDistance = 0;
		
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
//				if(squareRootDist>max)
//				{
//					max = squareRootDist;
//				}
				totalDistance += squareRootDist;
			}
		}
		System.out.println("total Distance distance between kernals: "+ totalDistance);
		averageDistance=totalDistance/numberOfSamples;
		return averageDistance;
	}

	
	/*
	 * This method calculates the distance between the sample input and the kernal set
	 * @param sample - each iuput sample
	 * @param kernalSet -  each hidden neuron
	 * @param variance - the width of gaussian function
	 * @return g - distance together with the gaussian function
	 */
	public static double[] sampleToKernalDist (double[] sample, ArrayList<double[][]> kernalSet, double variance)
	{
		double distance;
		double g=0.0;

		for(int i=0;i<kernalSet.size();i++)
		{
			distance = 0.0;
			for(int y=0; y<kernalSet.get(0)[0].length;y++)
			{
				distance += (sample[y]-kernalSet.get(i)[0][y])*(sample[y]-kernalSet.get(i)[0][y]);
			}
			double squareRootDist = Math.sqrt(distance);
			g = MathsUtils.gaussianRBF(squareRootDist, variance);
			hidden[i]= g;
		}
		return hidden;
	}
	
	/*
	 * This method set the kernal proportion
	 * @param prop- proportion
	 */
	public static void setKernalProportion(double prop)
	{
		kernalProportion = prop;
		System.out.println("kernal proportion: "+prop);
	}
	
	/*
	 * This method gets the accuracy of network
	 * @return accuracy - percentage of accuracy
	 */
	public static double getAccuracy()
	{
		return accuracy;
	}
	
	/*
	 * This method sets the number of iterations for the data to train
	 * @param e - the number of iterations
	 */
	public static void setEpoch (int e)
	{
		epoch = e ;
		System.out.println("Epoch: "+e);
	}

	/*
	 * This method gets the weights
	 * @return weightHO - weights between the hidden and output neurons.
	 */
	public static double[][] getWeights()
	{
		return weightHO;
	}
	

}
