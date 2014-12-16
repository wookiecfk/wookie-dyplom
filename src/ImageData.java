import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ImageData {

	public static List<ColourspacePixel> listOfReferencePicSquares = new ArrayList<ColourspacePixel>();
	public static List<ColourspacePixel> listOfComparedPicSquares = new ArrayList<ColourspacePixel>();
	
	public static List<ColourspacePixel> listOfReferencePicSquaresPostModification = new ArrayList<ColourspacePixel>();
	public static List<ColourspacePixel> listOfComparedPicSquaresPostModification = new ArrayList<ColourspacePixel>();
	
	public static List<ColourspacePixel> listOfReferencePicSquaresMidModification = new ArrayList<ColourspacePixel>();
	public static List<ColourspacePixel> listOfComparedPicSquaresMidModification = new ArrayList<ColourspacePixel>();
	
	public static List<Double> listOfReferenceGreen1 = new ArrayList<Double>();
	public static List<Double> listOfReferenceGreen2 = new ArrayList<Double>();
	public static List<Double> listOfReferenceBlue = new ArrayList<Double>();
	public static List<Double> listOfReferenceRed = new ArrayList<Double>();
	
	public static List<Double> listOfComparedGreen1 = new ArrayList<Double>();
	public static List<Double> listOfComparedGreen2 = new ArrayList<Double>();
	public static List<Double> listOfComparedBlue = new ArrayList<Double>();
	public static List<Double> listOfComparedRed = new ArrayList<Double>();
	
	public static void consolidateData(){
		ColourspacePixel pixel = null;
		double averageReferenceGreen = 0;
		double averageComparedGreen = 0;
		for(int i=0; i<listOfReferenceGreen1.size(); i++){
			averageReferenceGreen = (listOfReferenceGreen1.get(i) + listOfReferenceGreen2.get(i))/2;
			averageComparedGreen = (listOfComparedGreen1.get(i) + listOfComparedGreen2.get(i))/2;
			
			pixel = new ColourspacePixel(listOfReferenceRed.get(i), (double)averageReferenceGreen, (double)listOfReferenceBlue.get(i));
			listOfReferencePicSquares.add(pixel);
			
			pixel = new ColourspacePixel(listOfComparedRed.get(i), (double)averageComparedGreen, (double)listOfComparedBlue.get(i));
			listOfComparedPicSquares.add(pixel);
		}
	}
	
	public static void createCIELABValues(){
		for (int i=0; i<listOfReferencePicSquares.size(); i++){
			ColourspacePixel refPixelModified = new ColourspacePixel(listOfReferencePicSquares.get(i));
			ColourspacePixel compPixelModified = new ColourspacePixel(listOfComparedPicSquares.get(i));
			
			refPixelModified.transformPixelFromSrgbToXYZZausznicaMethod();
			
			listOfReferencePicSquaresMidModification.add(refPixelModified);
			
			refPixelModified.transformPixelFromXYZToCIELAB();
			compPixelModified.transformPixelFromSrgbToXYZZausznicaMethod();
			
			listOfComparedPicSquaresMidModification.add(compPixelModified);
			
			compPixelModified.transformPixelFromXYZToCIELAB();
			
			listOfReferencePicSquaresPostModification.add(refPixelModified);
			listOfComparedPicSquaresPostModification.add(compPixelModified);
		}
		
	}
	
	public static void printDataOutput(){
		for(int i=0; i<listOfReferencePicSquares.size(); i++){
			DecimalFormat df = new DecimalFormat("#.####");
			ColourspacePixel refPixel = listOfReferencePicSquares.get(i);
			ColourspacePixel compPixel = listOfComparedPicSquares.get(i);
			ColourspacePixel refLABPixel = listOfReferencePicSquaresPostModification.get(i);
			ColourspacePixel compLABPixel = listOfComparedPicSquaresPostModification.get(i);
			ColourspacePixel refXYZPixel = listOfReferencePicSquaresMidModification.get(i);
			ColourspacePixel compXYZPixel = listOfComparedPicSquaresMidModification.get(i);
			
			//TODO - write output
			
			//sRGB values
			System.out.println("Block nr" + (i+1) + " -->  Reference R: " + refPixel.value1 + ", Reference G: " + 
					refPixel.value2 + ", Reference B: " + refPixel.value3);
			System.out.println("Block nr" + (i+1) + " -->  Compared R: " + compPixel.value1 + ", Compared G: " + 
					compPixel.value2 + ", Compared B: " + compPixel.value3);
			System.out.println("Block nr" + (i+1) + " -->  Absolute Difference R: " + (refPixel.value1 - compPixel.value1) + ", Absolute Difference G: " + 
					(refPixel.value2 - compPixel.value2) + ", Absolute Difference B: " + (refPixel.value3 - compPixel.value3));
			System.out.println("Block nr" + (i+1) + " -->  Relative Difference R: " + df.format(Math.abs((refPixel.value1 - compPixel.value1)/refPixel.value1)) + ", Relative Difference G: " + 
					df.format(Math.abs((refPixel.value2 - compPixel.value2)/refPixel.value2)) + ", Relative Difference B: " + df.format(Math.abs(refPixel.value3 - compPixel.value3)/refPixel.value3));
			//XYZ values
			System.out.println("Block nr" + (i+1) + " -->  Reference x: " + refXYZPixel.value1 + ", Reference y: " + 
					refXYZPixel.value2 + ", Reference z: " + refXYZPixel.value3);
			System.out.println("Block nr" + (i+1) + " -->  Compared x: " + compXYZPixel.value1 + ", Compared y: " + 
					compXYZPixel.value2 + ", Compared z: " + compXYZPixel.value3);
			
			//CIELAB values
			System.out.println("Block nr" + (i+1) + " -->  Reference L: " + refLABPixel.value1 + ", Reference a: " + 
					refLABPixel.value2 + ", Reference b: " + refLABPixel.value3);
			System.out.println("Block nr" + (i+1) + " -->  Compared L: " + compLABPixel.value1 + ", Compared a: " + 
					compLABPixel.value2 + ", Compared b: " + compLABPixel.value3);
		}
	}
}
