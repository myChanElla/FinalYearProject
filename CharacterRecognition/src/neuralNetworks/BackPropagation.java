package neuralNetworks;

import guiPanels.DrawLineGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import utils.MathsUtils;
import data.ReadData;

/**
 * This class represent one of the neural networks which train the network via updating weights using back propagation.
 * Topology: Multi-layered perceptrons
 * Activation Function: Sigmoid function
 * Learning Process: Gradient Decent Learning
 * @author Chan Man Yi 1391904
 *
 */
public class BackPropagation
{
	
	//initialise the number of neurons in input, hidden and output layer.
	private static int INPUT_NEURON_NUM;
	private static int HIDDEN_NEURON_NUM;
	private static int OUTPUT_NEURON_NUM;
	
	//inititlise the input/hidden/output neuron
	private static double input[];
	private static double hidden[];
	private static double target[];
	private static double actual[];

	//initialise the weights
	//Input to Hidden Weights
	private static double weightIH[][];
	//Hidden neuron to output weights
	private static double weightHO[][];
	
	//initialise error for weight update
	private static double outputError[];
	private static double hiddenError[];
	
	//initialise the parameters.
	private static double LEARNING_RATE;
	private static int EPOCH;
	private static int MAX_COUNT;
	private static double totalError;
	private static double accuracy;
	
	/*
	 * This method trains the neural network
	 * @param trainingSet- training data set 
	 */
	public static void train(ArrayList<double[][]> trainingSet)
	{
		//assign random initial weights to weightIH[][] and weightHO[][]
		assignRandomWeights();
			
		//train the network
		input= new double [INPUT_NEURON_NUM];
		hidden = new double [HIDDEN_NEURON_NUM];
		target= new double [OUTPUT_NEURON_NUM];
		actual= new double [OUTPUT_NEURON_NUM];
		int count =0;
		MAX_COUNT = ReadData.getTrainingSet().size();
		
		for(int e=0;e<EPOCH;e++)
		{
			//repeat again when epoch is more than 256
			if(count == MAX_COUNT)
			{
				count = 0;
				double totalError = totalError();
				updateGraph(e, totalError);
			}
			
			//extract individual pixels values from a character as an input neuron
			for(int i=0;i<INPUT_NEURON_NUM; i++)
			{
				input[i]=trainingSet.get(count)[0][i];	
			}
			
			//extract the corresponding target output values of each characters into out put neurons
			for(int i=0;i<OUTPUT_NEURON_NUM;i++)
			{
				target[i]=trainingSet.get(count)[1][i];
			}

			//perform feed forward calculation.
			feedForward();
			
			//update weights via back propagation.
			backpropagation();	
			
			count +=1;
		}	
	}
	
	/*
	 * This method updates the graph showing the error
	 * @param e - epoch
	 * @param totalError - total error
	 */
	private static void updateGraph(int e, double totalError) 
	{
		DrawLineGraph.update(e, totalError);
		
	}

	/*
	 * This method calculate the total error
	 */
	public static double totalError()
	{
		totalError = 0.0;
		for(int out=0;out<OUTPUT_NEURON_NUM;out++)
		{
			totalError += (target[out]*Math.log(actual[out]))+((1-target[out])*Math.log(1-actual[out]));
		}
		return -totalError;
	}
	
	/*
	 * This Method test the neural network by inserting new testing data.
	 * @param testingSet-file containing the testing set.
	 */
	public static void test(ArrayList<double[][]> testingSet)
	{
		double trace =0;
		
		//put testing data into input neurons
		MAX_COUNT = ReadData.getTestingSet().size();
		for (int x=0;x<MAX_COUNT;x++)
		{
			for(int i=0;i<INPUT_NEURON_NUM;i++)
			{
				input[i] = testingSet.get(x)[0][i];
			}
			
			for(int t=0;t<OUTPUT_NEURON_NUM;t++)
			{
				target[t] = testingSet.get(x)[1][t];
			}
			
			feedForward();	
			
			//Check if actual output is target output
			//achieved by comparing the index of the outputs.
			if(index(actual)==index(target))
			{
				trace +=1;
			}
			else
			{
				//System.out.println("Actual output ("+indexToChara(index(actual))+ ") not matching target output("+indexToChara(index(target))+").");
				System.out.println("Target input "+ indexToChara(index(target))+ " was mistaken by "+ indexToChara(index(actual)));

			}
		}
		accuracy=(double)trace / (double)MAX_COUNT * 100.0;
        System.out.println("Network is " + accuracy + "% Correct");
	}
	
	/*
	 * This method returns the index of the output array(corresponding its value) which has the maximum value
	 */
	private static int index(final double[] output) 
	{
		int in =0;
		double max = output[in];
		for (int i=0;i<OUTPUT_NEURON_NUM;i++)
		{
			if(output[i]>max)
			{
				max=output[i];
				in=i;
			}
		}
		return in;
	}

	/*
	 * This method convert the index of the output into the representing character
	 * @param index- the index
	 * @return s[index]- the corresponding character 
	 */
	public static String indexToChara (int index)
	{
		String[] s = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		
		return s[index];
	}

	
	/*
	 * Update weights via back propagating from output layer back to input layer.
	 */
	private static void backpropagation() {
		
		//(Step 1)calculate error in output layer
		for(int x=0;x<OUTPUT_NEURON_NUM;x++){
			outputError[x]=(target[x]-actual[x])*(MathsUtils.sigmoidDerivative(actual[x]));
		}

		//(Step 2)update weight in output layer
		for(int o=0;o<OUTPUT_NEURON_NUM;o++){
			for(int h=0;h<HIDDEN_NEURON_NUM;h++){
				weightHO[h][o]+=(LEARNING_RATE*outputError[o]*hidden[h]);
			}
		}
		
		//(Step 3)calculate error in hidden layer
		for(int h=0;h<HIDDEN_NEURON_NUM;h++){
			hiddenError[h]=0.0;
			for(int out=0; out<OUTPUT_NEURON_NUM; out++){
				hiddenError[h]+= outputError[out] * weightHO[h][out];
			}
			hiddenError[h] *= (MathsUtils.sigmoidDerivative(hidden[h]));
		}

		//(Step 4)update weight in hidden layer
		for(int h=0;h<HIDDEN_NEURON_NUM;h++){
			for(int i=0;i<INPUT_NEURON_NUM;i++){
				weightIH[i][h]+=(LEARNING_RATE*hiddenError[h]*input[i]);
				//System.out.println(weightIH[i][h]);
			}
		}
		return;
	}

	/*
	 * calculate the output value of the hidden neuron using sigmoid function, and then calculate the weighted sum to get the actual output value.
	 *
	 */
	public static void feedForward() 
	{
		double weightedSumIH;
		double weightedSumHO;
		//(step 1)calculate the input value for each hidden neuron
		for(int h=0;h<HIDDEN_NEURON_NUM;h++)
		{
			weightedSumIH=0.0;
			for(int i=0; i<INPUT_NEURON_NUM;i++)
			{
				weightedSumIH += input[i]*weightIH[i][h];
			}
			hidden[h]=(MathsUtils.sigmoid(weightedSumIH));
		}
		
		//(step 2)calculate the output value
		for (int o=0;o<OUTPUT_NEURON_NUM;o++)
		{
			weightedSumHO=0.0;
			for(int h=0;h<HIDDEN_NEURON_NUM;h++)
			{
				weightedSumHO += hidden[h]*weightHO[h][o];
			}
			actual[o]=(MathsUtils.sigmoid(weightedSumHO));
		}
		return;
	}

	/*
	 * Before training, assign random weights in between neurons.
	 */
	public static void assignRandomWeights()
	{
		weightIH = new double [INPUT_NEURON_NUM][HIDDEN_NEURON_NUM];
		weightHO = new double [HIDDEN_NEURON_NUM][OUTPUT_NEURON_NUM];
		
		//weight between input and hidden layer
		for (int i=0; i<INPUT_NEURON_NUM; i++)
		{
			for(int j=0; j<HIDDEN_NEURON_NUM; j++)
			{
				double min = -0.3;
				double max = 0.3;
				Random r = new Random();
				double random = min + (max - min) * r.nextDouble();
				weightIH[i][j]= random;
			}
		}

		//weight between hidden to output layer
		for(int i=0;i<HIDDEN_NEURON_NUM; i++)
		{
			for(int j=0;j<OUTPUT_NEURON_NUM; j++)
			{
				double min = -0.3;
				double max = 0.3;
				Random r = new Random();
				double random = min + (max - min) * r.nextDouble();
				weightHO[i][j]=random;
			}
		}
		return;
	}
	
	/*
	 * This method set the number of hidden neuron
	 * @param num- number of input neurons
	 */
	public static void setInputNeuron(int num)
	{
		INPUT_NEURON_NUM = num;
		System.out.println("Number of input neurons: "+INPUT_NEURON_NUM);
	}

	/*
	 * This method sets the number of hidden neurons
	 * @param num - number of hidden neurons
	 */
	public static void setHiddenNeuron(int num)
	{
		HIDDEN_NEURON_NUM = num;
		hiddenError=new double[HIDDEN_NEURON_NUM];
		System.out.println("Number of hidden neurons: "+HIDDEN_NEURON_NUM);

	}
	
	/*
	 * This method sets the number of output neurons
	 * @param num - the number of output neurons
	 */
	public static void setOutputNeuron(int num)
	{
		OUTPUT_NEURON_NUM = num;
		outputError=new double[OUTPUT_NEURON_NUM];
		System.out.println("Number of output neurons: "+OUTPUT_NEURON_NUM);
	}

	/*
	 * This method sets learning rate
	 * @param r - the learning rate
	 */
	public static void setLearningRate(double r)
	{
		LEARNING_RATE = r;
	}
	
	/*
	 * This method sets the number of iterations for the data to train
	 * @param e - the number of iterations
	 */
	public static void setEpoch (int e)
	{
		EPOCH = e ;
		System.out.println("Epoch: "+e);
	}
	
	/*
	 * This method gets the mean square error of the network
	 */
	public static double getMSE()
	{
		return -totalError;
	}
	
	/*
	 * This method calculates the percentage of correctly recognising the testing data
	 */
	public static double getAccuracy()
	{
		System.out.println(accuracy);
		return Math.round((accuracy*100.0)/100.0);
	}
	
	/*
	 * This method gets the weights
	 */
	public static double[][] getWeights()
	{
		return weightHO;
	}


}
