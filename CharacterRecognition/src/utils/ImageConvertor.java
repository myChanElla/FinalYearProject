package utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/**
 * This class converts any images into a 900 pixels binary representation, allowing users to use their image as testing sets
 * User can convert images in a directory all at once
 * @author Chan Man Yi 1391904
 *
 */
public class ImageConvertor 
{
	private static JFileChooser fileChooser = new JFileChooser();
	private static Dimension cropImageSize;
	private static Dimension boundary = new Dimension(30, 30);
	private static int newWidth;
	private static int newHeight;
	private static File candidatefile;
	private static Writer candidateoutput = null;
	private static int target[];


	/*
	 * Constructor of class
	 * @param nameOfFile - file name which images inside will be converted.
	 */
	public ImageConvertor(String nameOfFile)
	{
		this.candidatefile=new File(nameOfFile+".txt");
		candidatefile.setWritable(true);
		try 
		{
			this.candidateoutput= new BufferedWriter(new FileWriter(candidatefile));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * This method reads each images in the folder
	 * @param folder - the chosen folder
	 */
	public static void readFolder(final File folder) throws IOException 
	{
		File cropFile = new File(folder+File.separator+"crop");
		cropFile.mkdir();
	    for (final File fileEntry : folder.listFiles()) 
	    {
	        if (fileEntry.isDirectory()) 
	        {
	        	System.out.println("reached directory");
	        } 
	        else if(fileEntry.getName().substring(fileEntry.getName().indexOf(".")).equals(".png")||fileEntry.getName().substring(fileEntry.getName().indexOf(".")).equals(".jpg"))
	        {
		          //trim the image to the character
	              try 
	              {         	  
	 
	            	BufferedImage image = ImageIO.read(fileEntry);
	            	String outputFileName = fileEntry.getParent()+File.separator+"crop"+File.separator+fileEntry.getName();
	                //Crop the image to the edge of the character.
	            	BufferedImage croppedImage = getCroppedImage(image,0.2);
	                
	            	//rescale the cropping image
	                cropImageSize = new Dimension(croppedImage.getWidth(), croppedImage.getHeight());
	        		BufferedImage b = resizeToBig(croppedImage);
	        		//fit(b, b.getType(), 30, 30, outputFileName);
	        		BufferedImage resized = fit(b, b.getType(), 30, 30);

	        		//convert into black and white image
	        		BufferedImage blackNWhite = convertToBlackWhite(resized, outputFileName);
	        		
	        		//convert the image into binary representation
	        		//read the target output of photo
	        		String n=fileEntry.getName().substring(0, 1);
	        		int targetIndex = charaToIndex(n);
	        		
	        		convert(blackNWhite);
	        		convertTarget(targetIndex);

	              }
	              catch (IOException ex) 
	              {
	                System.out.println("Error resizing the image.");
	                System.out.println("Image can't crop: "+ fileEntry);
	                ex.printStackTrace();
	              }
	        }
	        else 
	        {
	        }
	        String separator = System.getProperty("line.separator");
	        candidateoutput.write(separator);
	    }
	   //candidatefile.setReadOnly();
	    candidatefile.setWritable(false);
	    candidatefile.setReadOnly();
	}
	
	/*
	 * This method convert the target input into binary representation and write it onto the txt file.
	 * @param targetIndex - index of the target
	 */
	private static void convertTarget(int targetIndex) 
	{
		target=new int[52];
		target[targetIndex]=1;
		for(int m=0;m<target.length;m++)
		{
			try 
			{
				candidateoutput.write(target[m]+" ");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}

		}

		
	}

	/*
	 * This method trims the irrelevant content of the image away (edges)
	 * @param source - image source
	 * @param tolerance - colour tolerance, it allows images without perfect white background be cropped away, eg 2.0, colours up to 2% from the colour of the top left pixel will be cropped.
	 * @return destination - a buffered image of cropped image.
	 */
	public static BufferedImage getCroppedImage(BufferedImage source, double tolerance){
		   // Get our top-left pixel color as our "baseline" for cropping
		   int baseColor = source.getRGB(0, 0);
		   int width = source.getWidth();
		   int height = source.getHeight();

		   int topY = Integer.MAX_VALUE, topX = Integer.MAX_VALUE;
		   int bottomY = -1, bottomX = -1;
		   for(int y=0; y<height; y++) {
		      for(int x=0; x<width; x++) {
		         if (colorWithinTolerance(baseColor, source.getRGB(x, y), tolerance)){
		            //Decide the coordinates of the top and bottom pixel
		        	if (x < topX) topX = x;
		            if (y < topY) topY = y;
		            if (x > bottomX) bottomX = x;
		            if (y > bottomY) bottomY = y;
		         }
		      }
		   }
		   BufferedImage destination = new BufferedImage( (bottomX-topX+1), 
		                 (bottomY-topY+1), BufferedImage.TYPE_INT_ARGB);
		   Graphics2D g2d = destination.createGraphics();
		   g2d.drawImage(source, 0, 0, 
		               destination.getWidth(), destination.getHeight(), 
		               topX, topY, bottomX, bottomY, null);
		   return destination;
		}
	
	/*
	 * This method compares the rgb level of the top left pixel and the selected pixel to see if it has to be trimmed.
	 * @param a - rgb level of top left pixel
	 * @param b - pixel comparing
	 * @param tolerance - colour tolerance
	 * @return boolean - if this pixel have to be trimmed.
	 */
	private static boolean colorWithinTolerance(int a, int b, double tolerance) 
	{
	    int aAlpha  = (int)((a & 0xFF000000) >>> 24);   // Alpha level
	    int aRed    = (int)((a & 0x00FF0000) >>> 16);   // Red level
	    int aGreen  = (int)((a & 0x0000FF00) >>> 8);    // Green level
	    int aBlue   = (int)(a & 0x000000FF);            // Blue level

	    int bAlpha  = (int)((b & 0xFF000000) >>> 24);   // Alpha level
	    int bRed    = (int)((b & 0x00FF0000) >>> 16);   // Red level
	    int bGreen  = (int)((b & 0x0000FF00) >>> 8);    // Green level
	    int bBlue   = (int)(b & 0x000000FF);            // Blue level

	    double distance = Math.sqrt((aAlpha-bAlpha)*(aAlpha-bAlpha) +
	                                (aRed-bRed)*(aRed-bRed) +
	                                (aGreen-bGreen)*(aGreen-bGreen) +
	                                (aBlue-bBlue)*(aBlue-bBlue));

	    double percentAway = distance / 510.0d;     

	    return (percentAway > tolerance);
	}

	/*
	 * This method rescales the trimmed image to a 30x30 image 
	 * @param imgSize - size of the trimmed image
	 * @param boundary - the dimension of final image (30x30)
	 */
	public static void getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;
	    // first check if we need to scale width
	    if (original_width > bound_width) {
	       
	    	//scale width to fit
	        new_width = bound_width;
	        
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }
	    
	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	       
	    	//scale height to fit instead
	        new_height = bound_height;
	       
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }
	    
	    newWidth = new_width;
	    newHeight = new_height;
	}
	
	/*
	 * This method resizes the image
	 * @param croppedImage - the trimmed image
	 * @return resizedImage - new sized image
	 */
	public static BufferedImage resizeToBig(BufferedImage croppedImage) throws IOException 
	{
	   getScaledDimension(cropImageSize, boundary);
	    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, croppedImage.getType());
	    Graphics2D g = resizedImage.createGraphics();

	    //Specifies hoe new pixels are to be combined with the existing pixels on the graphics device.
	    g.setComposite(AlphaComposite.Src);
	    //controls how image pixels are resampled during image rendering operation - produce colour sample by 4 nearest neighboring integer coordinate samples (interpolated linearly) 
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    //Bias algorithm choices more for quality when evaluating trade off
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    //render is done with antialiasing - reduce aliasing artifacts along edges (make it smoother)
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    g.drawImage(croppedImage, 0, 0, newWidth, newHeight, null);
	    g.dispose();

	    return resizedImage;
	}
	
	/*
	 * This method add back white pixels inorder to scale the image to fit.
	 * @param image - the buffered image
	 * @param type - type of buffered image
	 * @param img_width - width of image
	 * @param img_height - height of image
	 * @return resizedCropImage - imaged rescaled
	 */
	public static BufferedImage fit(BufferedImage image, int type, Integer img_width, Integer img_height)
	{
		BufferedImage resizedCropImage = new BufferedImage(img_width, img_height, type);
		Graphics2D g = resizedCropImage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img_width, img_height);
		g.drawImage(image,((30-newWidth)/2), ((30-newHeight)/2),null);
		g.dispose();

		return resizedCropImage;
	}

	/*
	 * This image converts coloured images into black and white.
	 * @param coloredImage - original image
	 * @param outputImagePath - the file path in which the image is stored.
	 * @return blackNWhite - the converted image
	 */
	public static BufferedImage convertToBlackWhite(BufferedImage coloredImage, String outputImagePath)
	{
		BufferedImage blackNWhite = new BufferedImage(coloredImage.getWidth(),coloredImage.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D graphics = blackNWhite.createGraphics();
		graphics.drawImage(coloredImage, 0, 0, null);

      try 
      {
		ImageIO.write(blackNWhite, "png", new File(outputImagePath));
      } 
      catch (IOException e) 
      {
		e.printStackTrace();
      }
		return blackNWhite;
	}
	
    /*
     * This method turn the image into binary representation
     * @param im - buffered image
     */
	public static void convert(BufferedImage im) throws IOException
    {
  		
            for (int i = 0; i < im.getHeight(); i++) 
            {
                for (int j = 0; j < im.getWidth(); j++) 
                {
                    double color = im.getRGB(j, i);
                                  
                    if(color==-1)
                    {
                    	color = 0;
                    }
                    else
                    {
                    	color = 1;
                    }
                   
                    candidateoutput.write(color+" ");
                }
            }
    } 
    
    /*
     * This method returns the index of the character of the image
     * @param n - the character
     * @return helper - the index of this character
     */
	public static int charaToIndex(String n)
    {
		int helper = 0;
    	String[] s = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		
		for(int i=0;i<s.length;i++)
		{
			if(s[i].equals(n))
			{
				helper=i;
			}
		}
		return helper;
    }

}

