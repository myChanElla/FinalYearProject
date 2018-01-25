package guiPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 * This class is a pop up window teaching user how to train the RBF network.
 * @author ella chan
 *
 */
public class LearnRBF 
{
	private static int index =0;
	private String[] images;
	private JFrame frame;
	private JLabel imageLabel = new JLabel();
	
	private JButton next = new JButton("Next Step");
	private JButton previous = new JButton("Previous Step");
	private JButton backToFirst = new JButton("Back to First");
		
	/*
	 * Constructor of class
	 */
	public LearnRBF()
	{
		initialize();
	}
	
	/*
	 * This method gets the list of images in the directory.
	 * @return imageList - lists of images
	 */
	public String[] getImages()
	{
		File file = new File("./"+"RBF_images");
		System.out.println(file);

		String[] imageList = file.list();
		System.out.println("length of image list: "+imageList.length);
		return imageList;
	}
	
	/*
	 * This method gets the images and show it.
	 */
	public void showImage(int index1) 
	{
		images = getImages();
		
		String name = images[index1];
		System.out.println(name);
		
		ImageIcon icon = new ImageIcon("./"+"RBF_images/"+name);
		System.out.println(imageLabel.getWidth());
		System.out.println(imageLabel.getHeight());

		Image image = icon.getImage().getScaledInstance(550, 450, Image.SCALE_SMOOTH);
		
		 imageLabel.setIcon(new ImageIcon(image));
		
	}

	/*
	 * Method to set up the UI
	 */
	private void initialize() 
	{
		frame = new JFrame("Learn Radial Basis Function");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout());
		
		imageLabel.setBorder(new LineBorder(Color.BLACK));
		imageLabel.setPreferredSize(new Dimension(550,450));
		
		showImage(index);
		frame.add(imageLabel);
		
		frame.add(previous);
		previous.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
				{
					previousAction(e);
				}
		}
	  );

		frame.add(next);
		next.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
				{
					nextAction(e);
				}
		}
	    );

		frame.add(backToFirst);
		backToFirst.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
				{
					backToFirstAction(e);
				}
		}
		);
			
		
	}
	
	/*
	 * This method controls the previous button
	 */
	protected void previousAction(ActionEvent e) {
		index = index - 1;
		if(index<0)
		{
			index = 0;
		}
		showImage(index);
		
	}

	/*
	 * This method controls the next button
	 */
	protected void nextAction(ActionEvent e) {
		index = index + 1;
		System.out.println(index);
		if(index>=images.length)
		{
			System.out.println("exist list length");
			index = images.length -1;
		}
		showImage(index);
		
	}

	/*
	 * This method controls the button which brings back to the beginning
	 */
	protected void backToFirstAction(ActionEvent e) {
		index =0;
		showImage(index);
		
	}

	/*
	 * Set the frame to be visible.
	 */
	public void setVisible(boolean b) {
		frame.setVisible(b);
		
	}

}
