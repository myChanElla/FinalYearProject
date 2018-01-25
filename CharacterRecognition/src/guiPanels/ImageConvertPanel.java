package guiPanels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import utils.ImageConvertor;

/**
 * This class is a panel for showing the GUIs of converting images into binary representation.
 * @author Chan Man Yi 1391904
 *
 */
public class ImageConvertPanel extends JPanel
{
	private JButton chooseFileButton = new JButton("Choose File");
	private JLabel pathLabel = new JLabel();
	
	private JLabel nameOfFileLabel = new JLabel("Name of file: ");
	private JTextField nameOfFileTextField = new JTextField(30);
	
	private JButton convertButton = new JButton("Convert");
	private JProgressBar progressBar = new JProgressBar();
	private JLabel completedLabel = new JLabel("Convert completed!");
	
	private JFileChooser fileChooser = new JFileChooser();
	private File imageFile;
	
	/*
	 * Constructor of class
	 */
	public ImageConvertPanel()
	{
		super();
		setPreferredSize(new Dimension(780, 100));
		setBorder(BorderFactory.createTitledBorder("Convert character images"));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.weighty=0.5;
		
		//add JButton to choose file of images
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		chooseFileButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(chooseFileButton,gc);	
		chooseFileButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				imageFile=null;
		    	JOptionPane.showMessageDialog(null,"Please make sure images in your folder is named with the target output!");
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					imageFile = fileChooser.getSelectedFile();
					if(imageFile.isDirectory())
					{
						pathLabel.setText(imageFile.getPath());
						pathLabel.setVisible(true);
					}
					else
					{
				    	JOptionPane.showMessageDialog(null,"Please select a directory");
					}
					
				}
				else if(result == JFileChooser.CANCEL_OPTION)
				{
			
			    	pathLabel.setText("No file is chosen");
			    	pathLabel.setVisible(true);
				}
			}
		});
		
		
		
		//add name of file label
		gc.gridx = 0;
		gc.gridy = 1;
		nameOfFileLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		add(nameOfFileLabel,gc);
		
		//add text field for user to fill in the file name they want to name
		gc.gridx = 1;
		gc.gridy = 1;
		nameOfFileTextField.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(nameOfFileTextField,gc);

		//add convert button
		gc.gridx = 0;
		gc.gridy = 2;
		convertButton.setFont(new Font("sens-Serif", Font.BOLD, 12));
		add(convertButton,gc);
		convertButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String fileName = getFileName();
				if(fileName.isEmpty())
				{
			    	JOptionPane.showMessageDialog(null,"Please name the file you are about to convert");
			    	return;

				}
				ImageConvertor convertor = new ImageConvertor(fileName);
				class Myworker extends SwingWorker<String, Void>
				{

					@Override
					protected String doInBackground() throws Exception 
					{
						if(imageFile != null)
						{
							progressBar.setVisible(true);
							progressBar.setIndeterminate(true);

							try
							{
								convertor.readFolder(imageFile);			
								completedLabel.setVisible(true);
							}
							catch(IOException e)
							{
								System.out.println("something wrong with the folder");
								e.printStackTrace();

							}

						}
						else
						{
					    	JOptionPane.showMessageDialog(null,"Please select a directory first");

						}
						
						return "Done";
					}
					protected void done()
					{
						progressBar.setIndeterminate(false);
						progressBar.setVisible(false);
					}
					
					
				}
				new Myworker().execute();		
			}
			
		});
		
		//completion label to indicate user convert is completed
		gc.gridx = 1;
		gc.gridy = 2;
		completedLabel.setFont(new Font("sens-Serif", Font.BOLD, 14));
		completedLabel.setVisible(false);
		add(completedLabel,gc);

		//progress bar
		gc.gridx = 1;
		gc.gridy = 2;
		add(progressBar,gc);
		
		//add file path when user chosen a file
		gc.gridx = 1;
		gc.gridy = 0;
		gc.gridheight=1;
		gc.gridwidth=2;
		pathLabel.setFont(new Font("sens-Serif", Font.BOLD, 12));
		pathLabel.setVisible(false);
		add(pathLabel,gc);

	}
	
	/*
	 * This method gets the textfield filled by user
	 * return String in the text field
	 */
	public String getFileName()
	{
		return nameOfFileTextField.getText();
	}
}
