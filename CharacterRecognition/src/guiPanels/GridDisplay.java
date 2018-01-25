package guiPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.ReadData;

/**
 * This Class is a JPanel containing the upper part of the Main interface.  Allows users to choose a data set and then it will display the data set on screen.
 * @author Chan Man Yi 1391904
 *
 */
public class GridDisplay extends JPanel //implements ListSelectionListener 
{
	private Grid grid;
	private JList itemList;
	private static String[] characters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
											"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	private ArrayList<double[]> temporary = new ArrayList<double[]>(); 
	private static int flag;//keep track of which grid is showing.
	
	private JButton chooseDataButton = new JButton("Choose Data Set");
	private JButton previous = new JButton("<");
	private JButton next = new JButton(">");
	
	public JLabel path = new JLabel();
	public JLabel displayNum = new JLabel();
	public JLabel dataDisplay = new JLabel();
	int displayNumber;

	/*
	 * Constructor of class
	 */
	public GridDisplay(Grid grid)
	{
		super();
		this.grid = grid;
		setPreferredSize(new Dimension(780,400));
		setBorder(BorderFactory.createTitledBorder("Data Set Display"));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		//Create JButton to choose data set
		gc.weightx=0.5; //how much space it is taking up
		gc.weighty=0.5; 
		gc.gridx = 0; //Where to start
		gc.gridy = 0;
		add(chooseDataButton, gc);
		
		path.setVisible(false);
		gc.anchor = GridBagConstraints.EAST;
		add(path,gc);
	

		//JList for selecting the the data 
		itemList = new JList(characters);
		gc.anchor=GridBagConstraints.CENTER;
		itemList.setFixedCellWidth(250);
		itemList.setVisibleRowCount(17);
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gc.gridx = 0;
		gc.gridy = 1;
		add(new JScrollPane(itemList), gc);
			
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 0;
		dataDisplay.setVisible(false);
		add(dataDisplay,gc);
		
		//Grid showing the character visually.
		gc.anchor=GridBagConstraints.CENTER;
		gc.gridwidth=2;
		gc.gridx = 1;
		gc.gridy = 1;
		add(grid, gc);
		
		//Previous Button
		gc.gridwidth=1;
		gc.anchor=GridBagConstraints.LINE_END;
		gc.gridx = 1;
		gc.gridy = 2;
		add(previous, gc);
		
		//Next Button
		gc.anchor=GridBagConstraints.LINE_START;
		gc.gridx = 2;
		gc.gridy = 2;
		add(next,gc);
		
	}

	/*
	 * This method gets the index of the list item
	 * @return index of list
	 */
	public int getListItem()
	{
		return itemList.getSelectedIndex();
	}
	
	/*
	 * This method clears the temporary array
	 */
	public void clearTemporaryArray()
	{
		temporary.clear();
	}
	
	/*
	 * This class returns the temporary array
	 * @return temporary- the temporary array
	 */
	public ArrayList<double[]> getTempArray()
	{
		return temporary;
	}
	
	/*
	 * This method gets the flag on which character is showing
	 * @return flag- the flag number
	 */
	public int getFlag()
	{
		return flag;
	}
	
	/*
	 * This method sets which flag is showing
	 * @return flag- the number of character it is showing
	 */
	public int setFlag(int x)
	{
		return flag = x;
	}
	
	/*
	 * This mothod sets the label in the GUI
	 */
	public void setLabel(String labelName)
	{
		path.setText(labelName);
		path.setVisible(true);
	}
	
	/*
	 * This method selects the method 
	 */
	public void selectedIndex()
	{
		itemList.setSelectedIndex(0);
	}
	
	/*
	 * This method sets the number for displaying on GUI
	 */
	public void setDisplayNumber(int number)
	{
		displayNumber= number;
		dataDisplay.setText(number+" of "+ (getTempArray().size()));
		dataDisplay.setVisible(true);
	}
	
	/*
	 * This method clears the diaplay number
	 */
	public void clearDisplayNumber()
	{
		dataDisplay.setVisible(false);
	}
	
	/*
	 * This method clears the item selection in item list
	 */
	public void clearItemSelection()
	{
		itemList.clearSelection();
	}
	
	/*
	 * Add listener method to call the action listener class when user click the choose data button
	 */
	void addChooseDataSetListener(ActionListener listenForChooseDataButton)
	{
		chooseDataButton.addActionListener(listenForChooseDataButton);
		
	}
	
	/*
	 * Add listener method to call the list selection listener class when user choose the list item.
	 */
	void addChooseListListener(ListSelectionListener listenForItemList)
	{
		itemList.addListSelectionListener(listenForItemList);
	}

	/*
	 * Add listener method to call the action listener class when the next button is clicked.
	 */
	void addNextListener(ActionListener listenForNextButton)
	{
		next.addActionListener(listenForNextButton);
	}
	
	/*
	 * Add listener method to call the action listener class when the previous button is clicked.
	 */
	void addPreviousListener(ActionListener listenForPreviousButton)
	{
		previous.addActionListener(listenForPreviousButton);
	}


}
