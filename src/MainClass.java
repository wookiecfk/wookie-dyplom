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
    String path;
    if (args.length==0) {
      path = JOptionPane.showInputDialog(null, "Image Path",
      "/home/l.krolak/Desktop/Untitled Folder/dyplom-wookie/DSC00060.tiff");
    }
    else {
      path = args[0];
    }
    FileInputStream in = new FileInputStream(path);
    FileChannel channel = in.getChannel();
    ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
    //all image operations have to be done on buffer.array()
    channel.read(buffer);
    //Lets split it 
    byte[] bufferSplit = Arrays.copyOf(buffer.array(), (buffer.array().length));
    
    RenderedImage im = load(bufferSplit);
    
    //MY SHIT
    RenderedImage postProcessingImageFirstColour = splitColour(im, 00);
    RenderedImage postProcessingImageFirstColourModified = splitColour(im, 00);
//    RenderedImage postProcessingImageSecondColour = splitColour(im, 01);
//    RenderedImage postProcessingImageThirdColour = splitColour(im, 10);
//    RenderedImage postProcessingImageFourthColour = splitColour(im, 11);
    
    //Do some postprocessing on postProcessingImageFirstColourModified
    postProcessingImageFirstColourModified = ImageAdapter.runAlgorithm(postProcessingImageFirstColourModified, 0, 0);
    
    
    //MY SHIT END
    Image imageFirst = PlanarImage.wrapRenderedImage(postProcessingImageFirstColour).getAsBufferedImage();
    Image imageFirstModified = PlanarImage.wrapRenderedImage(postProcessingImageFirstColourModified).getAsBufferedImage();
//    Image imageSecond = PlanarImage.wrapRenderedImage(postProcessingImageSecondColour).getAsBufferedImage();
//    Image imageThird = PlanarImage.wrapRenderedImage(postProcessingImageThirdColour).getAsBufferedImage();
//    Image imageFourth = PlanarImage.wrapRenderedImage(postProcessingImageFourthColour).getAsBufferedImage();
    
    // make sure that the image is not too big
    //  scale with a width of 500 
    imageScaledFirst = imageFirst.getScaledInstance(1000, -1,  Image.SCALE_SMOOTH);
    imageScaledFirstModified = imageFirstModified.getScaledInstance(1000, -1,  Image.SCALE_SMOOTH);
//    imageScaledSecond = imageSecond.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
//    imageScaledThird = imageThird.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
//    imageScaledFourth = imageFourth.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
    //
    //System.out.println("image: " + path + "\n" + image);
    //
//    Thread t1 = new Thread(new Runnable(){
//    	@Override
//    	public void run() {
//    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledFirst)), "Green1", JOptionPane.INFORMATION_MESSAGE);
//    	}
//    });
//    t1.start();
    
    Thread t1Modified = new Thread(new Runnable(){
    	@Override
    	public void run() {
    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledFirstModified)), "Green1 - Modified", JOptionPane.INFORMATION_MESSAGE);
    	}
    });
    t1Modified.start();
    
    
//    Thread t2 = new Thread(new Runnable(){
//    	@Override
//    	public void run() {
//    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledSecond)), "Blue", JOptionPane.INFORMATION_MESSAGE);
//    	}
//    });
//    t2.start();
//    
//    Thread t3 = new Thread(new Runnable(){
//    	@Override
//    	public void run() {
//    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledThird)), "Red", JOptionPane.INFORMATION_MESSAGE);
//    	}
//    });
//    t3.start();
//    
//    Thread t4 = new Thread(new Runnable(){
//    	@Override
//    	public void run() {
//    		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(imageScaledFourth)), "Green2", JOptionPane.INFORMATION_MESSAGE);
//    	}
//    });
//    t4.start();
  }
}