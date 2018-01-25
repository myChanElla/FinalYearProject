package guiPanels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/**
 * This class is a JPanel containing the lower part of the main Intferface which
 * allow users to set neural network parameters to train the data. 
 * @author Chan Man Yi 1391904
 *
 */

public class ParameterPanel extends JPanel
{
	
	private JLabel numOfHiddenLabel = new JLabel("Hidden neurons: ");
	private JLabel learningRateLabel = new JLabel("Learning Rate: ");
	private JLabel epochLabel = new JLabel("Epoch: ");
	
	private JTextField hiddenNeuronTextBox = new JTextField(10);
	private JTextField learningRateTextBox = new JTextField(10);
	private JTextField epochTextBox = new JTextField(10);
	
	private JButton trainButton = new JButton("Train");
	private JProgressBar trainProgressBar = new JProgressBar();
	
	private JLabel mseLabel = new JLabel("Cost Function: ");
	private JLabel mseNumLabel = new JLabel();
	
	private JLabel timeTakenTrainLabel = new JLabel("Time Taken: ");
	private JLabel timeTakenTrainNumLabel = new JLabel();
	
	private JLabel trainingCompleteLabel = new JLabel();
	private JButton testButton = new JButton("Test!");
	private JProgressBar testProgressBar = new JProgressBar();
	
	private JLabel accuracyLabel = new JLabel("Accuracy: ");
	private JLabel accuracyNumLabel = new JLabel();
	
	private JLabel timeTakenTestLabel = new JLabel("Time Taken: ");
	private JLabel timeTakenTestNumLabel = new JLabel();
	
	private JButton learnButton = new JButton("How This network works?");
	
	/*
	 * constructor, initiate the JPanel
	 */
	public ParameterPanel()
	{
		super();
		setPreferredSize(new Dimension(380,250));
		setBorder(BorderFactory.createTitledBorder("Multi-Layered Network with Back Propagation"));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();		

		gc.anchor=GridBagConstraints.LINE_START;
		
		gc.gridx = 0;
		gc.gridy = 0;
		numOfHiddenLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(numOfHiddenLabel,gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		add(hiddenNeuronTextBox,gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		learningRateLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(learningRateLabel,gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		add(learningRateTextBox,gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		epochLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(epochLabel,gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		add(epochTextBox,gc);

		
		gc.gridx = 0;
		gc.gridy = 3;
		trainButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(trainButton,gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		add(trainProgressBar,gc);
		
		gc.gridx = 0;
		gc.gridy = 4;
		mseLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(mseLabel,gc);
		
		gc.gridx = 1;
		gc.gridy = 4;
		mseNumLabel.setFont(new Font("sens-Serif", Font.PLAIN, 14));
		add(mseNumLabel,gc);
		
		gc.gridx = 0;
		gc.gridy = 5;
		timeTakenTrainLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(timeTakenTrainLabel,gc);
		
		gc.gridx = 1;
		gc.gridy = 5;
		timeTakenTrainNumLabel.setFont(new Font("sens-Serif", Font.PLAIN, 14));
		add(timeTakenTrainNumLabel,gc);
		
		gc.gridx = 0;
		gc.gridy = 6;
		testButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(testButton,gc);
		
		gc.gridx = 1;
		gc.gridy = 6;
		add(testProgressBar,gc);
		
		gc.gridx = 0;
		gc.gridy = 7;
		accuracyLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(accuracyLabel,gc);
		
		gc.gridx = 1;
		gc.gridy = 7;
		accuracyNumLabel.setFont(new Font("sens-Serif", Font.PLAIN, 14));
		add(accuracyNumLabel,gc);

		gc.gridx = 0;
		gc.gridy = 8;
		timeTakenTestLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(timeTakenTestLabel,gc);

		gc.gridx = 1;
		gc.gridy = 8;
		timeTakenTestNumLabel.setFont(new Font("sens-Serif", Font.PLAIN, 14));
		add(timeTakenTestNumLabel,gc);

		gc.anchor=GridBagConstraints.CENTER;
		gc.gridwidth=2;
		gc.gridx = 0;
		gc.gridy = 9;
		learnButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(learnButton,gc);
		learnButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				LearnBackProp learn = new LearnBackProp();
				learn.setVisible(true);	
			} 
			} );

	}
	
	/*
	 * This method gets the number of hidden neuron user enters
	 */
	public int getHiddenNeuron()
	{
		System.out.println("get hidden neuron from users");
		if(hiddenNeuronTextBox.getText().isEmpty())
		{
	    	JOptionPane.showMessageDialog(null,"Hidden Neuron field cannot be emptied!");
		}
		return Integer.parseInt(hiddenNeuronTextBox.getText());
	}
	
	/*
	 * This method gets the learning rate input by user
	 */
	public double getLearningRate()
	{
		System.out.println("get learning rate from user");
		if(learningRateTextBox.getText().isEmpty())
		{
	    	JOptionPane.showMessageDialog(null,"Learning Rate field cannot be emptied!");
		}
		return Double.parseDouble(learningRateTextBox.getText());
	}
	
	/*
	 * This Method gets the epoch input by users
	 */
	public int getEpoch()
	{
		System.out.println("Number of EPOCH: "+epochTextBox.getText());
		if(epochTextBox.getText().isEmpty())
		{
	    	JOptionPane.showMessageDialog(null,"Epoch field cannot be emptied!");
		}
		return Integer.parseInt(epochTextBox.getText());
	}
	
	/*
	 * This method show the labels upon completing training
	 */
	public void showTrainingComplete()
	{
		trainingCompleteLabel.setText("<html><font color='red'>Training Completed!</font></html>");
		trainingCompleteLabel.setVisible(true);
		testButton.setVisible(true);
	}
	
	/*
	 * If the train button is clicked execute the method in the controller named action performed
	 */
	void addTrainListener(ActionListener listenForTrainButton)
	{
		trainButton.addActionListener(listenForTrainButton);
	}
	
	/*
	 * If the test button is clicked execute the method in the controller named action performed
	 */
	void addTestListener(ActionListener listenForTestButton)
	{
		testButton.addActionListener(listenForTestButton);
	}

	
	/*
	 * This method create a indeterminate training progress bar
	 * @param value- boolean value determine whether the progress bar is visible or not.
	 */
	public void setTrainProgressBar(boolean value)
	{
		trainProgressBar.setVisible(value);
		trainProgressBar.setIndeterminate(value);
	}
	
	/*
	 * This method create a indeterminate testing progress bar
	 * @param value - see if the progress bar is visible, as well as see if progress bar is indeterminate.
	 */
	public void setTestProgressBar(boolean value)
	{
		testProgressBar.setVisible(value);
		testProgressBar.setIndeterminate(value);
	}
	
	/*
	 * This methos set the mean square error during the training process
	 */
	public void setMSE (String value)
	{
		mseNumLabel.setText(value);
	}
	
	/*
	 * This method set training time label
	 * @param time- time taken
	 */
	public void setTrainTime (long time)
	{
		timeTakenTrainNumLabel.setText(String.valueOf(time/1000)+" Seconds");
	}
	
	/*
	 * This methos set testing time label
	 * @param time - time taken
	 */
	public void setTestTime (long time)
	{
		if(time<1000)
		{
			timeTakenTestNumLabel.setText("Less than a second");
		}
		else 
		{
			timeTakenTestNumLabel.setText(String.valueOf(time/1000)+" Seconds");
		}
	}
	
	/*
	 * This method set the accuracy value
	 */
	public void setAccuracy (double percentage)
	{
		accuracyNumLabel.setText(String.valueOf(percentage)+ "%");
	}
	
	/*
	 * This method throws an error message when users enter the wrong input
	 */
	void displayErrorMessage(String errorMessage)
	{
		JOptionPane.showMessageDialog(this, errorMessage);
	}
	
	/*
	 * This method made train button not clickable
	 */
	public void cannotClickTrainButton()
	{
		trainButton.setEnabled(false);
	}
	
	/*
	 * This method made train button clickable
	 */
	public void canClickTrainButton()
	{
		trainButton.setEnabled(true);
	}
	
	/*
	 * This method made test button not clickable
	 */
	public void cannotClickTestButton()
	{
		testButton.setEnabled(false);
	}
	
	/*
	 * This method made train button clickable
	 */
	public void canClickTestButton()
	{
		testButton.setEnabled(true);
	}


}
