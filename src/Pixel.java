import java.awt.image.WritableRaster;


public class Pixel extends Location{
	
	//for 16-bit pixel RGB values
	static final int maxValueOfPixel = 65535;
	static final float offsetValue = (float)0.05;
	
	int valueOfPixel;
	
	protected Pixel(int mPositionX, int mPositionY, int valueOfPixel){
		super(mPositionX, mPositionY);
		this.valueOfPixel = valueOfPixel;
	}
	
	protected Pixel(int mPositionX, int mPositionY, int valueOfPixel, int maxValueOfPixel){
		this(mPositionX, mPositionY, valueOfPixel);
	}
	
	protected Pixel(){
		super(0,0);
	}

	public int getValueOfPixel() {
		return valueOfPixel;
	}

	public void setValueOfPixel(int valueOfPixel) {
		this.valueOfPixel = valueOfPixel;
	}
	
	//Compares pixel value for similarity with specified offset and returns true if they are *similar* (as in from the same square)
	protected boolean comparePixels(Pixel pixel){
		if(valueOfPixel>(pixel.valueOfPixel-(maxValueOfPixel*offsetValue)) && (valueOfPixel<(pixel.valueOfPixel+(maxValueOfPixel*offsetValue)))){
			return true;
		}
		return false;
	}
	//Same as comparePixels but working directly on Pixel integer value
	protected boolean compareToPixelFromValue(int pixelValue){
		if(valueOfPixel>(pixelValue-(maxValueOfPixel*offsetValue)) && (valueOfPixel<(pixelValue+(maxValueOfPixel*offsetValue)))){
			return true;
		}
		return false;
	}
	
	static Pixel getPixelFromLocation(Location location, WritableRaster writableRaster){
		int[] valueArray = new int[1];
		valueArray = writableRaster.getPixel(location.getPositionX(), location.getPositionY(), valueArray);	
		Pixel pixelFromLocation = new Pixel(location.getPositionX(), location.getPositionY(), valueArray[0]);
		return pixelFromLocation;
	}
		
}


