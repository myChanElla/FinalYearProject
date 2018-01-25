package guiPanels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

/**
 * This class is responsible for plotting the mean square error line graph with respect to the number of epochs during training.
 * @author Chan Man Yi 1391904
 *
 */
public class DrawLineGraph 
{
	private static int epoch = 0;
	private static double totalError = 0;
	private static JFrame frame;
	private static XYSeries series;
	private static JFreeChart chart;
	
	
	/*
	 * Constructor of the class
	 */
	public DrawLineGraph()
	{
		frame = new JFrame();
		frame.setTitle("Line Graph");
		frame.setSize(600,400);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		series = new XYSeries("Cross Entropy Cost Function");
		XYSeriesCollection dataset = new XYSeriesCollection(series);
		chart = ChartFactory.createXYLineChart("Cross Entropy Cost Function", "Epoch", "Error", dataset);
		frame.add(new ChartPanel(chart), BorderLayout.CENTER);
		

	}
	

	/*
	 * This method updates the chart
	 * @param e - epoch, number of iterations
	 * @param Error - mean square error
	 */
	public static void update(int e, double error) 
	{
		epoch = e;
		totalError = error;
		series.add(e, error);
		frame.revalidate();
		frame.repaint();
	}
	
	/*
	 * This method sets the graph to be visible
	 * @param b - True of False for the frame to be visible.
	 */
	public void setVisible(boolean b) {
		frame.setVisible(b);	
	}

	
}
