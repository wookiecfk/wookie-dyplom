import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

import javax.media.jai.PlanarImage;


public class ImageAdapter {
	static int minPixelValue=20000;
	static int maxPixelValue=20000;
	
	static RenderedImage runAlgorithm(RenderedImage fullImage, int position1, int position2, String path, String colour, boolean isReference){
		//Premodifications
		PlanarImage fullChangeableImage = PlanarImage.wrapRenderedImage(fullImage);
		Raster raster = fullChangeableImage.getData();
		WritableRaster writableRaster = raster.createCompatibleWritableRaster();
		
		  for(int y=0; y<writableRaster.getHeight()-1; y++,y++){
			  for(int x=0; x<writableRaster.getWidth()-1; x++,x++){
				  int[] pixArray = new int[1];
				  pixArray=raster.getPixel(x+position1, y+position2, pixArray);
				  
//				  if(minPixelValue>pixArray[0]){
//					  minPixelValue=pixArray[0];
//				  }
//				  if(maxPixelValue<pixArray[0]){
//					  maxPixelValue=pixArray[0];
//				  }
					  
				  writableRaster.setPixel(x, y, pixArray);
				  writableRaster.setPixel(x, y+1, pixArray);
				  writableRaster.setPixel(x+1, y, pixArray);
				  writableRaster.setPixel(x+1, y+1, pixArray);
			  }
		  }
		  //System.out.println(minPixelValue);
		  //System.out.println(maxPixelValue);
		//Run processing algorithms here
		
		Algorithms.mainAlgorithmTest(writableRaster, path, colour, isReference);
		
		//Postmodifications
		BufferedImage fullChangedImage = fullChangeableImage.getAsBufferedImage();
		fullChangedImage.setData(writableRaster);
		return fullChangedImage;
	}

	public static int getMinPixelValue() {
		return minPixelValue;
	}

	public static int getMaxPixelValue() {
		return maxPixelValue;
	}
	
}
