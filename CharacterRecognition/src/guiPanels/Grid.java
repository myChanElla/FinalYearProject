package guiPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.*;

import data.ReadData;

/**
 * This class build the grid to show characters on the gui
 * @author Chan Man Yi 1391904
 *
 */
public class Grid extends JPanel{
	private final static int rows=30; 
	private final static int columns=30;
	private static Color[][] gridColor=new Color[rows][columns];
	private Color lineColor=Color.black;
	
	/*
	 * Constructor of class
	 */
	public Grid()
	{
		super();		
		setBackground(Color.WHITE);
		setPreferredSize( new Dimension(300,300));	//320
	}

	/*
	 * Paint component class
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g)
	{
		int r,c;
		double cellWidth = 10; //20
		double cellHeight = 10;
		g.setColor(getBackground());
		g.fillRect(0, 0,(int) (cellWidth*rows),(int) (cellHeight*columns));

		for(r=0;r<rows;r++)
		{
			for (c=0;c<columns;c++)
			{
				if (gridColor[r][c] != null) 
				{
					int x1 = (int)(c*cellWidth);
					int y1 = (int)(r*cellHeight);
					int x2 = (int)((c+1)*cellWidth);
					int y2 = (int)((r+1)*cellHeight);
					g.setColor(gridColor[r][c]);
					g.fillRect( x1, y1, (x2-x1), (y2-y1) );
				}
			}
		}
		
		g.setColor(lineColor);
		for(r=0;r<=rows;r++)
		{
			for(c=0;c<=columns;c++)
			{
				int y = (int)(r*cellHeight);
				int x = (int)(c*cellWidth);
				g.drawLine(0,y,(int) (cellWidth*rows),y);
				g.drawLine(x,0,x,(int) (cellHeight*columns));
			}
		}
	}

	/*
	 * The method clears and repaints the grid
	 */
	public void clearGrid() 
	{
		for(int r=0;r<rows;r++)
		{
			for(int c=0;c<columns;c++)
			{
				gridColor[r][c]= Color.WHITE;
			}
		}
		validate();
		repaint();
	}

	/*
	 * This method shows the grid
	 * @param temp- array list of characters
	 * @param item- index of character wanting to show
	 */
	public void showGrid(ArrayList<double[]> temp, int item) 
	{
		try
		{
			int x=0;
				for(int r=0;r<rows;r++)
				{
					for(int c=0;c<columns;c++)
					{
						
							if(temp.get(item)[x]==1.0)
							{
								
								gridColor[r][c]= Color.BLACK;
							}	
							x++;					
					}
				}
			
			validate();
			repaint();
		}
		catch(Exception e)
		{
	    	JOptionPane.showMessageDialog(null,"This is not a valid data set txt file, please choose another valid file");
		}

	}

}
