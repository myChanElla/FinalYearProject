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
 * This class represents the panel for controlling the parameter for constructing the radial basis function network.
 * @author Chan Man Yi 1391904
 *
 */
public class RadialParameterPanel extends JPanel
{	
	private JLabel centreProportion = new JLabel("Proportion: ");
	private JTextField centreProportionTextField = new JTextField(10);
	
	private JLabel epoch = new JLabel("Epoch: ");
	private JTextField epochTextField = new JTextField(10);
	
	private JButton trainButton = new JButton("Train");
	private JProgressBar trainProgressBar = new JProgressBar();
	
	private JLabel timeTakenTrainLabel = new JLabel("Time Taken: ");
	private JLabel timeTakenTrainNumLabel = new JLabel();
	
	private JButton testButton = new JButton("Test!");
	private JProgressBar testProgressBar = new JProgressBar();
	
	private JLabel accuracyLabel = new JLabel("Accuracy: ");
	private JLabel accuracyNumLabel = new JLabel();
	
	private JLabel timeTakenTestLabel = new JLabel("Time Taken: ");
	private JLabel timeTakenTestNumLabel = new JLabel();

	private JButton learnButton = new JButton("How This network works?");

	/*
	 * Constructor of class
	 */
	public RadialParameterPanel()
	{
		super();
		setPreferredSize(new Dimension(380,250));
		setBorder(BorderFactory.createTitledBorder("RBF Network"));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
						
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 0;
		gc.gridy = 1;
		centreProportion.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(centreProportion,gc);
		
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 1;
		add(centreProportionTextField,gc);

		gc.gridx = 0;
		gc.gridy = 2;
		epoch.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(epoch,gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		add(epochTextField,gc);

		gc.gridx = 0;
		gc.gridy = 3;
		trainButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(trainButton,gc);
		
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 3;
		add(trainProgressBar,gc);
		
		gc.gridx = 0;
		gc.gridy = 4;
		timeTakenTrainLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(timeTakenTrainLabel,gc);
		
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 4;
		timeTakenTrainNumLabel.setFont(new Font("sens-Serif", Font.PLAIN, 14));
		add(timeTakenTrainNumLabel,gc);
		
		gc.gridx = 0;
		gc.gridy = 5;
		testButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(testButton,gc);
		
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 5;
		add(testProgressBar,gc);
		
		gc.gridx = 0;
		gc.gridy = 6;
		accuracyLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(accuracyLabel,gc);
		
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 6;
		accuracyNumLabel.setFont(new Font("sens-Serif", Font.PLAIN, 14));
		add(accuracyNumLabel,gc);
		
		gc.gridx = 0;
		gc.gridy = 7;
		timeTakenTestLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(timeTakenTestLabel,gc);
		
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 7;
		timeTakenTestNumLabel.setFont(new Font("sens-Serif", Font.PLAIN, 14));
		add(timeTakenTestNumLabel,gc);
		
		gc.anchor=GridBagConstraints.CENTER;
		gc.gridwidth=2;
		gc.gridx = 0;
		gc.gridy = 8;
		learnButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(learnButton,gc);
		learnButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				LearnRBF learn = new LearnRBF();
				learn.setVisible(true);	
			} 
			} );
	}
	
	/*
	 * This class gets the proportion of kernal set which users input.
	 * @return centreProportionTextField.getText() - kernal proportion user inserts.
	 */
	public double getProportion()
	{
		if(centreProportionTextField.getText().isEmpty())
		{
	    	JOptionPane.showMessageDialog(null,"Centre Proportion field cannot be emptied!");
		}
		return Double.parseDouble(centreProportionTextField.getText());
	}
	
	/*
	 * This Method gets the epoch input by users
	 * @return epoch - epoch user inserts
	 */
	public int getEpoch()
	{
		System.out.println("Number of EPOCH: "+epochTextField.getText());
		if(epochTextField.getText().isEmpty())
		{
	    	JOptionPane.showMessageDialog(null,"Epoch field cannot be emptied!");
		}
		return Integer.parseInt(epochTextField.getText());
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
	 * This method set training time label
	 * @param time- time taken
	 */
	public void setTrainTime (long time)
	{
		if(time<1000)
		{
			timeTakenTrainNumLabel.setText("Less than a second");
		}
		else 
		{
			timeTakenTrainNumLabel.setText(String.valueOf(time/1000)+" Seconds");
		}
	}

	/*
	 * This method set testing time label
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
		accuracyNumLabel.setText(String.valueOf(percentage)+"%");
	}
	
	/*
	 * This method throws an error message when users enter the wrong input
	 */
	void displayErrorMessage(String errorMessage)
	{
		JOptionPane.showMessageDialog(this, errorMessage);
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

	/*
	 * This method made train button not clickable
	 */
	public void cannotClickTrainButton()
	{
		trainButton.setEnabled(false);
	}
}
