package guiPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import neuralNetworks.BackPropagation;
import neuralNetworks.RadialBasisFunction;
import data.ReadData;

/**
 * This class represents a panel which allows users to input 
 * a data set of characters and display it on a grid of squares.
 * This class also allows users to preview the data set they have chosen.
 * @author Chan Man Yi 1391904
 *
 */
public class DisplayPanel extends JFrame
{
	private GridDisplay gridDisplay;
	private Controller controller;
	private Grid grid;
	private ReadData readData;
	private ParameterPanel parameterPanel;
	private RadialParameterPanel rbfParameterPanel;
	private ImageConvertPanel imageConvertPanel;
	private BackPropagation backPropagation;
	private RadialBasisFunction radialBasisFunction;
	private RadialParameterPanel rbf;
	
	/*
	 * Constructor of class
	 */
	public DisplayPanel(String title)
	{
		super(title);
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		//Create Swing Component
		grid = new Grid();
		readData = new ReadData();
		gridDisplay = new GridDisplay(grid);
		parameterPanel=new ParameterPanel();
		rbf = new RadialParameterPanel();
		imageConvertPanel = new ImageConvertPanel();
		controller = new Controller(gridDisplay, readData, grid, parameterPanel, rbf);
		
		gc.gridwidth = 2; 
		gc.gridx = 0; 
		gc.gridy = 0;
		add(imageConvertPanel, gc);

		gc.gridwidth = 2; 
		gc.gridx = 0; 
		gc.gridy = 1;
		add(gridDisplay, gc);
		
		gc.gridwidth = 1;
		gc.gridx=0;//0;
		gc.gridy=2;//1;
		add(parameterPanel,gc);
		
		gc.gridx=1;//1;
		gc.gridy=2;//1;
		add(rbf,gc);

	}
	
}
