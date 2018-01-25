package mainGUI;
import guiPanels.Controller;
import guiPanels.DisplayPanel;
import guiPanels.Grid;
import guiPanels.GridDisplay;

import javax.swing.*;

import data.ReadData;

import java.awt.*;
import java.io.File;
/**
 * This class contains the main method of the GUI
 * @author Chan Man Yi 1391904
 *
 */
public class MainGUI 
{
	private static ReadData r = new ReadData();

	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				
				JFrame frame = new DisplayPanel("Character Recognition");
				frame.setSize(800,800);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				r.openFile(new File("Train.txt"));
				r.readTrainingSet();
				r.closeFile();
				System.out.println("Train file imported");
			}
		});
	}
	
}
