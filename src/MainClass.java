import javax.media.jai.PlanarImage;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class MainClass  {
	static Image imageScaledFirst, imageScaledFirstModified, imageScaledSecond, imageScaledThird, imageScaledFourth;
	static Image imageScaled2First, imageScaled2FirstModified, imageScaled2Second, imageScaled2Third, imageScaled2Fourth;

  static RenderedImage load(byte[] data) throws Exception{
    Image image = null;
    SeekableStream stream = new ByteArraySeekableStream(data);
    String[] names = ImageCodec.getDecoderNames(stream);
    ImageDecoder dec = 
      ImageCodec.createImageDecoder(names[0], stream, null);
    RenderedImage im = dec.decodeAsRenderedImage();
    return im;
  }
  
  // number - 00 for first, 01 for second, 10 for third, 11 for fourth of data grid
  static RenderedImage splitColour(RenderedImage fullImage, int number){
	  PlanarImage fullChangeableImage = PlanarImage.wrapRenderedImage(fullImage);
	  Raster raster = fullChangeableImage.getData();
	  WritableRaster writableRaster = raster.createCompatibleWritableRaster();
	  
	  int position1=0, position2=0;
	  switch(number){
	  	case 00: 
	  		position1=0;
	  		position2=0;
	  		break;
	  	case 01:
	  		position1=0;
	  		position2=1;
	  		break;
	  	case 10:
	  		position1=1;
	  		position2=0;
	  		break;
	  	case 11:
	  		position1=1;
	  		position2=1;
	  		break;
	  }
	  for(int y=0; y<writableRaster.getHeight()-1; y++,y++){
		  for(int x=1; x<writableRaster.getWidth()-1; x++,x++){
			  int[] pixArray = new int[1];
			  pixArray=raster.getPixel(x+position1, y+position2, pixArray);
			  writableRaster.setPixel(x, y, pixArray);
			  writableRaster.setPixel(x, y+1, pixArray);
			  writableRaster.setPixel(x+1, y, pixArray);
			  writableRaster.setPixel(x+1, y+1, pixArray);
		  }
	  }
	  
	  BufferedImage fullChangedImage = fullChangeableImage.getAsBufferedImage();
	  fullChangedImage.setData(writableRaster);
	  return fullChangedImage;
  }

  public static void main(String[] args) throws Exception{
    String path, path2;
    if (args.length==0) {
      path = JOptionPane.showInputDialog(null, "Reference Image Path",
      "/home/wookieee/Desktop/dyplom/testphotosnikon/DSC_0041.tiff");
      path2 = JOptionPane.showInputDialog(null, "Compared Image Path",
    	      "/home/wookieee/Desktop/dyplom/testphotosnikon/DSC_0041.tiff");
    }
    else {
      path = args[0];
      path2 = args[0];
    }
    //Reference image
    FileInputStream in1 = new FileInputStream(path);
    FileChannel channel1 = in1.getChannel();
    ByteBuffer buffer1 = ByteBuffer.allocate((int)channel1.size());
    //all image operations have to be done on buffer.array()
    channel1.read(buffer1);
    //Lets split it 
    byte[] bufferSplit1 = Arrays.copyOf(buffer1.array(), (buffer1.array().length));
    
    RenderedImage im = load(bufferSplit1);
    
    //Image being compared
    FileInputStream in2 = new FileInputStream(path2);
    FileChannel channel2 = in2.getChannel();
    ByteBuffer buffer2 = ByteBuffer.allocate((int)channel2.size());
    //all image operations have to be done on buffer.array()
    channel2.read(buffer2);
    //Lets split it 
    byte[] bufferSplit2 = Arrays.copyOf(buffer2.array(), (buffer2.array().length));
    
    RenderedImage im2 = load(bufferSplit2);
    
    //MY SHIT - Reference image
    RenderedImage postProcessingImageFirstColour = splitColour(im, 00);
    RenderedImage postProcessingImageSecondColour = splitColour(im, 01);
    RenderedImage postProcessingImageThirdColour = splitColour(im, 10);
    RenderedImage postProcessingImageFourthColour = splitColour(im, 11);
    
    //MY SHIT - Image being compared
    RenderedImage postProcessingImage2FirstColour = splitColour(im2, 00);
    RenderedImage postProcessingImage2SecondColour = splitColour(im2, 01);
    RenderedImage postProcessingImage2ThirdColour = splitColour(im2, 10);
    RenderedImage postProcessingImage2FourthColour = splitColour(im2, 11);
    
    //Do the postprocessing and loading data
    postProcessingImageFirstColour = ImageAdapter.runAlgorithm(postProcessingImageFirstColour, 0, 0, path, "Green1", true);
    postProcessingImageSecondColour = ImageAdapter.runAlgorithm(postProcessingImageSecondColour, 0, 0, path, "Blue", true);
    postProcessingImageThirdColour = ImageAdapter.runAlgorithm(postProcessingImageThirdColour, 0, 0, path, "Red", true);
    postProcessingImageFourthColour = ImageAdapter.runAlgorithm(postProcessingImageFourthColour, 0, 0, path, "Green2", true);
    
    postProcessingImage2FirstColour = ImageAdapter.runAlgorithm(postProcessingImage2FirstColour, 0, 0, path2, "Green1", false);
    postProcessingImage2SecondColour = ImageAdapter.runAlgorithm(postProcessingImage2SecondColour, 0, 0, path2, "Blue", false);
    postProcessingImage2ThirdColour = ImageAdapter.runAlgorithm(postProcessingImage2ThirdColour, 0, 0, path2, "Red", false);
    postProcessingImage2FourthColour = ImageAdapter.runAlgorithm(postProcessingImage2FourthColour, 0, 0, path2, "Green2", false);
    
    //Wrapping into showable form
    Image imageFirst = PlanarImage.wrapRenderedImage(postProcessingImageFirstColour).getAsBufferedImage();
    Image imageSecond = PlanarImage.wrapRenderedImage(postProcessingImageSecondColour).getAsBufferedImage();
    Image imageThird = PlanarImage.wrapRenderedImage(postProcessingImageThirdColour).getAsBufferedImage();
    Image imageFourth = PlanarImage.wrapRenderedImage(postProcessingImageFourthColour).getAsBufferedImage();
    
    Image image2First = PlanarImage.wrapRenderedImage(postProcessingImage2FirstColour).getAsBufferedImage();
    Image image2Second = PlanarImage.wrapRenderedImage(postProcessingImage2SecondColour).getAsBufferedImage();
    Image image2Third = PlanarImage.wrapRenderedImage(postProcessingImage2ThirdColour).getAsBufferedImage();
    Image image2Fourth = PlanarImage.wrapRenderedImage(postProcessingImage2FourthColour).getAsBufferedImage();
    
    // make sure that the image is not too big
    //  scale with a width of 500 
    imageScaledFirst = imageFirst.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    imageScaledSecond = imageSecond.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    imageScaledThird = imageThird.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    imageScaledFourth = imageFourth.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    
    imageScaled2First = image2First.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    imageScaled2Second = image2Second.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    imageScaled2Third = image2Third.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    imageScaled2Fourth = image2Fourth.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    //
    //System.out.println("image: " + path + "\n" + image);
    //
    //Print output!
    ImageData.consolidateData();
    ImageData.createCIELABValues();
    ImageData.printDataOutput();
    Thread t1 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledFirst)), "Green1 Reference", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t1.start();
 
    Thread t2 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledSecond)), "Blue Reference", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t2.start();

    Thread t3 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledThird)), "Red Reference", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t3.start();
 
    Thread t4 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledFourth)), "Green2 Reference", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t4.start();
    
    Thread t5 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaled2First)), "Green1 Compared", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t5.start();
 
    Thread t6 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaled2Second)), "Blue Compared", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t6.start();

    Thread t7 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaled2Third)), "Red Compared", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t7.start();
 
    Thread t8 = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaled2Fourth)), "Green2 Compared", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t8.start();
  }
}