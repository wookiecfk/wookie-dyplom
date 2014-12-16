
public class ColourspacePixel {
	
	//D50 tristimulus XYZ values
	static double whiteX=0.9642;
	static double whiteY=1.0000;
	static double whiteZ=0.8249;
	public double value1;		///RED - X - L
	public double value2;		//GREEN - Y - A
	public double value3;		//BLUE - Z - B
	
	public ColourspacePixel(double value1, double value2, double value3) {
		super();
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}
	
	public ColourspacePixel(ColourspacePixel colourspacePixel) {
		this.value1 = colourspacePixel.value1;
		this.value2 = colourspacePixel.value2;
		this.value3 = colourspacePixel.value3;
	}

	void transformPixelFromSrgbToXYZDxaMethod(){
		final double value1Before = value1;
		final double value2Before = value2;
		final double value3Before = value3;
		
		// value X
		value1 = 0.4124*(Math.pow((value1Before/65535),2.2)) + 0.3576*(Math.pow((value2Before/65535),2.2)) +
				 0.1805*(Math.pow((value3Before/65535),2.2));
		// value Y
		value2 = (double)0.2126*((double)Math.pow(((double)value1Before/(double)65535),(double)2.2)) + (double)0.7152*((double)Math.pow(((double)value2Before/(double)65535),(double)2.2)) +
				 (double)0.0722*((double)Math.pow(((double)value3Before/(double)65535),(double)2.2));
		// value Z
		value3 = 0.0193*(Math.pow((value1Before/65535),2.2)) + 0.1192*(Math.pow((value2Before/65535),2.2)) +
				 0.9505*(Math.pow((value3Before/65535),2.2));
	}
	
	void transformPixelFromSrgbToXYZZausznicaMethod(){
		final double value1Before = value1;
		final double value2Before = value2;
		final double value3Before = value3;
		
		// value X
		value1 = 0.4124*(value1Before/65535) + 0.3576*(value2Before/65535) +
				 0.1805*(value3Before/65535);
		// value Y
		value2 = 0.2126*(value1Before/65535) + 0.7152*(value2Before/65535) +
				 0.0722*(value3Before/65535);
		// value Z
		value3 = 0.0193*(value1Before/65535) + 0.1192*(value2Before/65535) +
				 0.9505*(value3Before/65535);
	}
	
	void transformPixelFromXYZToCIELAB(){
		final double value1Before = value1;
		final double value2Before = value2;
		final double value3Before = value3;
		
		double X = functionXYZCIELAB(value1Before/whiteX);
		double Y = functionXYZCIELAB(value2Before/whiteY);
		double YLightnessOnly = functionXYZCIELABLightnessOnly(value2Before/whiteY);
		double Z = functionXYZCIELAB(value3Before/whiteZ);
		
		value1 = ((double)116 * (double)YLightnessOnly) - (double)16;
		value2 = (double)500 * ((double)X - (double)Y);
		value3 = (double)200 * ((double)Y - (double)Z);
	}
	
	double functionXYZCIELAB(double argument){
		if((double)argument>(double)0.008856){
			return (double)Math.pow((double)argument, (double)1/3);
		}
		else{
			return (double)(((double)7.787*(double)argument) + ((double)16/(double)116));
		}
	}
	
	double functionXYZCIELABLightnessOnly(double argument){
		if((double)argument>(double)0.008856){
			return (double)Math.pow((double)argument, (double)1/3);
		}
		else{
			return (double)((double)903.3*(double)argument);
		}
	}
}
