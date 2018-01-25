package guiPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import neuralNetworks.BackPropagation;
import neuralNetworks.RadialBasisFunction;
import data.ReadData;

/**
 * This class serves as a controller in the whole GUI responsible to handle all action performed throughout the GUI
 * @author Chan Man Yi 1391904
 *
 */
public class Controller 
{
	private GridDisplay gridDisplayView;
	private JFileChooser fileChooser = new JFileChooser();
	private int f ;
	private int p;
	private ParameterPanel parameterView;
	private RadialParameterPanel rbfParameterView;
	
	
	/*
	 * Constructor- handling all action listener, and action performed
	 * @gridDisplayView - the grid display panel
	 * @readDataModel - the read data class
	 * @grid - the grid panel
	 * @parameterView - the parameter panel for back propagation algorithm
	 * @rbfParameterView - the parameter panel for radial basis function algorithm
	 * 
	 */
	public Controller(GridDisplay gridDisplayView, ReadData readDataModel, Grid grid, ParameterPanel parameterView, RadialParameterPanel rbfParameterView)
	{
		this.gridDisplayView = gridDisplayView;
		this.parameterView=parameterView;
		this.rbfParameterView = rbfParameterView;
		new BackPropagation();
		new RadialBasisFunction();
		
		
//-------------------------------------- ACTION LISTNER FOR BUTTONS IN THE CHOOSE DATA SET PANEL------------------------------------		
		//Button listener for chossing the data set
		this.gridDisplayView.addChooseDataSetListener(new ActionListener()
															{
																@Override
																public void actionPerformed(ActionEvent e) 
																{
																	grid.clearGrid();
																	gridDisplayView.clearDisplayNumber();

																	fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
																	
																	int result = fileChooser.showOpenDialog(null);
																		if (result == JFileChooser.APPROVE_OPTION) 
																		{
																			
																		    File trainingData = fileChooser.getSelectedFile();
																		    if(trainingData.getName().substring(trainingData.getName().indexOf(".")).equals(".txt"))
																		    {

																		    	gridDisplayView.setLabel("Reading Data...");
																		    	
																				class MyWorker extends SwingWorker<String, Void>
																				{

																					@Override
																					protected String doInBackground() throws Exception 
																					{
																						try
																						{
																						    readDataModel.openFile(trainingData);
																						    readDataModel.readFile();
																						    readDataModel.closeFile();
																							gridDisplayView.setLabel("Data is Ready!");
																						}
																						catch(Exception ex)
																						{
																							System.out.println(ex);
																						}
																						return "Done";
																					}
																				}
																				new MyWorker().execute();
																		    }
																		    else
																		    {
																		    	JOptionPane.showMessageDialog(null,"Please select the right document, dataset should be in .txt file");
																		    }
																	
																	   }
																	   else if(result == JFileChooser.CANCEL_OPTION)
																	   {
																			gridDisplayView.setLabel("No file is chosen!");

																	   }
				
																}
			
															});

		//Button Listener for next button 
		this.gridDisplayView.addNextListener(new ActionListener()
												{
													@Override
													public void actionPerformed(ActionEvent e) 
													{
														f = gridDisplayView.getFlag();
														grid.clearGrid();
														if(gridDisplayView.getTempArray().isEmpty())
														{
															return;
														}
														else
														{
															if(f<(gridDisplayView.getTempArray().size()))
															{
																f = gridDisplayView.setFlag(f+1);
																gridDisplayView.setDisplayNumber(f);
																
																grid.showGrid(gridDisplayView.getTempArray(), f-1);
															}
															else
															{
																f = gridDisplayView.setFlag(f);
																grid.showGrid(gridDisplayView.getTempArray(), f-1);
															}
														}
													
													}
												});
		
		//Action listener for previous button
		this.gridDisplayView.addPreviousListener(new ActionListener()
													{
														@Override
														public void actionPerformed(ActionEvent e) 
														{
															p = gridDisplayView.getFlag();
															grid.clearGrid();
															if(gridDisplayView.getTempArray().isEmpty())
															{
																return;
															}
															else
															{
																if(p>1)
																{
																	p = gridDisplayView.setFlag(p-1);
																	gridDisplayView.setDisplayNumber(p);
																	grid.showGrid(gridDisplayView.getTempArray(), p-1);
																}
																else
																{
																	p = gridDisplayView.setFlag(p);
																	grid.showGrid(gridDisplayView.getTempArray(), p-1);
																}		
															}
														}
													});

		
		//Action listener for selecting characters in the JList
		this.gridDisplayView.addChooseListListener(new ListSelectionListener()
														{
															@Override
															public void valueChanged(ListSelectionEvent e) 
															{
																int value = gridDisplayView.getListItem();
																gridDisplayView.clearTemporaryArray();
																int size = readDataModel.getDataSet().size();
															
																int i;
																for(i=0;i<size-1;i++)
																{
																	if(readDataModel.getDataSet().get(i)[1][value]==1)
																	{
																		gridDisplayView.getTempArray().add(readDataModel.getDataSet().get(i)[0]);
																	}
																}
																grid.clearGrid();
																int flag = gridDisplayView.setFlag(1);
																if(gridDisplayView.getTempArray().isEmpty())
																{
																	if(readDataModel.getDataSet().isEmpty())
																	{
																		JOptionPane.showMessageDialog(null,"This is not a valid data set txt file, please choose another valid file");
																	}
																}
																else
																{
																	grid.showGrid(gridDisplayView.getTempArray(), flag-1);
																}
																
																gridDisplayView.setDisplayNumber(1);

															}
													});
		
//-------------------------------------- ACTION LISTNER FOR BUTTONS IN THE MLP+BACKPROPAGATION PANEL------------------------------------		
		//Train button listener
		this.parameterView.addTrainListener(new ActionListener()
												{
													@Override
													public void actionPerformed(ActionEvent e) 
													{
														class MyWorker extends SwingWorker<String, Void>
														{

															@Override
															protected String doInBackground()
																	throws Exception {
																int hidden=0;
																double learningRate=0;
																int epoch = 0;
																try
																{
																	DrawLineGraph draw = new DrawLineGraph();
																	draw.setVisible(true);
																	hidden = parameterView.getHiddenNeuron();
																	learningRate = parameterView.getLearningRate();
																	epoch = parameterView.getEpoch();
																	BackPropagation.setHiddenNeuron(hidden);
																	BackPropagation.setLearningRate(learningRate);
																	BackPropagation.setEpoch(epoch);
																	BackPropagation.setInputNeuron(readDataModel.getTrainingSet().get(1)[0].length);
																	BackPropagation.setOutputNeuron(readDataModel.getTrainingSet().get(1)[1].length);
																	parameterView.setTrainProgressBar(true);
																	parameterView.cannotClickTestButton();
																	parameterView.cannotClickTrainButton();
																	long start = System.currentTimeMillis();
																	System.out.println("timer started...");
																	BackPropagation.train(readDataModel.getTrainingSet());
																	long end= System.currentTimeMillis();
																	parameterView.setTrainTime(end-start);
																	System.out.println("Time taken to train network: " + (end-start) + " milliseconds");
																	double mse = BackPropagation.getMSE();
																	parameterView.setMSE(Double.toString(mse));
																	parameterView.canClickTestButton();
																	parameterView.canClickTrainButton();
																	//parameterView.showTrainingComplete();
																}
																catch(NumberFormatException ex)
																{
																	parameterView.displayErrorMessage("Invalid parameter type! \n Hidden Neurons: integer \n Learning Rate: double between 0 and 1 \n Epoch: integer");
																}
																return "Done";
															}
															
															protected void done()
															{
																parameterView.setTrainProgressBar(false);
															}
															
														}
														
														new MyWorker().execute();
													}
												});
		
		//Test Button Listener
		this.parameterView.addTestListener(new ActionListener()
												{
													@Override
													public void actionPerformed(ActionEvent e) 
													{
														parameterView.cannotClickTestButton();
														parameterView.cannotClickTrainButton();	
														fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
												
														int result = fileChooser.showOpenDialog(null);
														parameterView.setTestProgressBar(true);
												
															if (result == JFileChooser.APPROVE_OPTION) 
															{
															    File testingData = fileChooser.getSelectedFile();
															    if(testingData.getName().substring(testingData.getName().indexOf(".")).equals(".txt"))
															    {
																	try
																	{
																		readDataModel.clearTestingSet();
																		readDataModel.openFile(testingData);
																		readDataModel.readTestingSet();
																		readDataModel.closeFile();
																	}
																	catch(Exception ex)
																	{
																		System.out.println(ex);
																	}
															    }
															    else
															    {
															    	JOptionPane.showMessageDialog(null,"Please select the right document, dataset should be in .txt file");
															    }
														   }
														   else if(result == JFileChooser.CANCEL_OPTION)
														   {
																gridDisplayView.setLabel("No file is chosen!");
														   }
														
														class MyWorker extends SwingWorker<String, Void>
														{
															@Override
															protected String doInBackground()
																	throws Exception {
																try
																{	
																	if(BackPropagation.getWeights()==null)
																	{
																		parameterView.displayErrorMessage("Please train network first before you test it!");
																	}
																	else
																	{	
																		long start = System.currentTimeMillis();
																		BackPropagation.test(readDataModel.getTestingSet());
																		long end= System.currentTimeMillis();
																		System.out.println("Time taken to test network: " + (end-start) + " milliseconds");
																		parameterView.setTestTime(end-start);
																		double percentage = BackPropagation.getAccuracy();
																		parameterView.setAccuracy(percentage);
																		parameterView.canClickTestButton();
																		parameterView.canClickTrainButton();
																	}
																}
																catch(NumberFormatException ex)
																{
																	System.out.println(ex);
																	parameterView.displayErrorMessage("Please fill in the text fields before training!");
																}
																return "Done";
															}
															
															protected void done()
															{
																parameterView.setTestProgressBar(false);
															}
														}
														
														new MyWorker().execute();
													}
												});
		
//-------------------------------------- ACTION LISTNER FOR BUTTONS IN THE RBF PANEL------------------------------------		

		//Train button listener
		this.rbfParameterView.addTrainListener(new ActionListener()
												{
													@Override
													public void actionPerformed(ActionEvent e)
													{
														rbfParameterView.setTrainProgressBar(true);
														class MyWorker extends SwingWorker<String, Void>
														{
															@Override
															protected String doInBackground()
																	 {
																try
																{	
																	double p = rbfParameterView.getProportion();	
																	RadialBasisFunction.setKernalProportion(p);
																	int epoch = rbfParameterView.getEpoch();
																	RadialBasisFunction.setEpoch(epoch);
																	rbfParameterView.cannotClickTestButton();
																	rbfParameterView.cannotClickTrainButton();
																	long start = System.currentTimeMillis();
																	RadialBasisFunction.train(readDataModel.getTrainingSet());
																	long end= System.currentTimeMillis();
																	rbfParameterView.canClickTestButton();
																	rbfParameterView.canClickTrainButton();
																	System.out.println("Time taken to train network: " + (end-start) + " milliseconds");
																	rbfParameterView.setTrainTime(end-start);
																}
																catch(Exception ex)
																{
																	ex.printStackTrace();
																	System.out.println(ex);
																	//rbfParameterView.displayErrorMessage("Invalid parameter type! \n Centre Proportion: double between 0 and 1 \n Epoch: integer");
																}
																return "Done";
															}
															
															protected void done()
															{
																rbfParameterView.setTrainProgressBar(false);
															}
														}
														new MyWorker().execute();									
													}
												});
		
		//Test button Listener
		this.rbfParameterView.addTestListener(new ActionListener()
													{
														@Override
														public void actionPerformed(ActionEvent e) 
														{
															
															rbfParameterView.cannotClickTestButton();
															rbfParameterView.cannotClickTrainButton();	
															fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
															int result = fileChooser.showOpenDialog(null);
											
															if (result == JFileChooser.APPROVE_OPTION) 
															{
															    File testingData = fileChooser.getSelectedFile();
															    if(testingData.getName().substring(testingData.getName().indexOf(".")).equals(".txt"))
															    {
																	try
																	{
																		readDataModel.clearTestingSet();
																		readDataModel.openFile(testingData);
																		readDataModel.readTestingSet();
																		readDataModel.closeFile();
																	}
																	catch(Exception ex)
																	{
																		System.out.println(ex);
																	}
															    }
																else
																{
																   	JOptionPane.showMessageDialog(null,"Please select the right document, dataset should be in .txt file");
																}
															}
															else if(result == JFileChooser.CANCEL_OPTION)
															{
																gridDisplayView.setLabel("No file is chosen!");
															}
															rbfParameterView.setTestProgressBar(true);
																
															class MyWorker extends SwingWorker<String, Void>
															{
																@Override
																protected String doInBackground()
																		throws Exception {
																	try
																	{
																		if(RadialBasisFunction.getWeights()==null)
																		{
																			rbfParameterView.displayErrorMessage("Please train network first before you test it!");
																		}
																		else
																		{
																			long start = System.currentTimeMillis();
																			RadialBasisFunction.test(readDataModel.getTestingSet());
																			long end= System.currentTimeMillis();
																			System.out.println("Time taken to test network: " + (end-start) + " milliseconds");
																			rbfParameterView.setTestTime(end-start);
																			double percentage = RadialBasisFunction.getAccuracy();
																			rbfParameterView.canClickTestButton();
																			rbfParameterView.canClickTrainButton();
																			rbfParameterView.setAccuracy(percentage);
																		}
																	}
																	catch(NumberFormatException ex)
																	{
																		System.out.println(ex);
																		rbfParameterView.displayErrorMessage("Please fill in the text fields before training!");
																	}
																	return "Done";
																}
																
																protected void done()
																{
																	rbfParameterView.setTestProgressBar(false);
																}
																
															}
															
															new MyWorker().execute();
														}
													});


	}
	
}

