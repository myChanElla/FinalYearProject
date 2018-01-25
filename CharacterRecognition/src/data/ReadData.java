package data;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import neuralNetworks.BackPropagation;

/**
 * This class is responsible to read data sets and cast them to suitable data type for manipulation.
 * @author Chan Man Yi 1391904
 *
 */
public class ReadData {
	private Scanner x;
	private static ArrayList<double[][]>trainingSet = new ArrayList<double[][]>();
	private static ArrayList<double[][]>testingSet = new ArrayList<double[][]>();
	private static ArrayList<double[][]>dataSet;
	private double[] input;
	private double[] output;
	
	/*
	 * Constructor 
	 */
	public ReadData()
	{
		super();
	}
	
	/*
	 * This method opens the file by introducing a scanner object.
	 */
	public void openFile(File fileName)
	{
		try
		{
			x = new Scanner (fileName);
		}
		catch(Exception e)
		{
			System.out.println("file not found");
		}	
	}
	
	/*
	 * This method reads the data from the scanner
	 */
	public void readFile()
	{	
		try
		{
			dataSet = new ArrayList<double[][]>();
			System.out.println(x.hasNext());
			while(x.hasNext())
			{
				//extract 256 values from the file, this is the input neurons.
				input = new double[900];
				for(int i=0;i<input.length;i++)
				{
					input[i]=x.nextDouble();
				}
				
				//Extract the following 10 digits, that represent the target output.
				output= new double[52];
				for(int j=0;j<output.length;j++)
				{
					output[j]=x.nextInt();
				}
				
				//put the 2 1D arrays into an 2D array, which represents one sample
				double oneSample[][]={input, output};
				dataSet.add(oneSample);	
			}
			System.out.println("The size of dataSet: "+trainingSet.size());
		}
		catch(Exception e)
		{
			e.getMessage();
		}	
	}

	/*
	 * This class reads the training sets from the scanner
	 */
	public void readTrainingSet()
	{	
		while(x.hasNext())
		{
			//extract 256 values from the file, this is the input neurons.
			input = new double[900];
			for(int i=0;i<input.length;i++)
			{
				input[i]=x.nextDouble();
			}
			
			//Extract the following 10 digits, that represent the target output.
			output= new double[52];
			for(int j=0;j<output.length;j++)
			{
				output[j]=x.nextInt();
			}
			
			//put the 2 1D arrays into an 2D array, which represents one sample
			double oneSample[][]={input, output};
			trainingSet.add(oneSample);				
		}
		
		System.out.println("The size of Training set: "+trainingSet.size());
		
	}
	
	/*
	 * This class reads the testing sets from the scanner
	 */
	public void readTestingSet()
	{	
		while(x.hasNext())
		{
			//extract 256 values from the file, this is the input neurons.
			input = new double[900];
			for(int i=0;i<input.length;i++)
			{
				input[i]=x.nextDouble();
			}
			
			//Extract the following 10 digits, that represent the target output.
			output= new double[52];
			for(int j=0;j<output.length;j++)
			{
				output[j]=x.nextInt();
			}
			
			//put the 2 1D arrays into an 2D array, which represents one sample
			double oneSample[][]={input, output};
			testingSet.add(oneSample);				
		}
		
		System.out.println("The size of Testing set: "+testingSet.size());
		
	}

	/*
	 * This method closes the file by closing the scanner object
	 */
	public void closeFile()
	{
		x.close();
	}
	
	/*
	 * This method gets the training set
	 * @return trainingSet - the training set
	 */
	public static ArrayList<double[][]> getTrainingSet()
	{
		return trainingSet;
	}

	/*
	 * This method gets the testing set
	 * @return testingSet - the testing set
	 */
	public static ArrayList<double[][]> getTestingSet()
	{
		return testingSet;
	}
	
	/*
	 * This method gets the data set
	 * @return dataSet - the dataSet
	 */
	public static ArrayList<double[][]> getDataSet()
	{
		return dataSet;
	}
	
	/*
	 * This method clears the data sets.
	 */
	public static void clearTestingSet()
	{
		testingSet.clear();
	}

}
