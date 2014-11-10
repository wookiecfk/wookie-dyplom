import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

public class Algorithms {
	
	/*
	 * Edge offset for algorithm in going *through* edges (entering a new field)
	 */
	static final int edgeOffset = 10; 
	static final int dataSquareSize = 20;

	/*
	 * Main algorithm of the class, it iterates through every square on the photo and gets colour data from it to be processed later
	 */
	static void mainAlgorithm(WritableRaster imageRaster){
		int[] pixArray = new int[1];
		Pixel blackPixel=null;
		
		searchingForBlackPixelLoop:
		/*
		 * 
		 * Iterating through every pixel searching for a black one - with values (at least close) to 0.
		 * Going from left to right on outside loop and top to bottom on inside loop should land me on square (1,2).
		 * **** ADDED **** Additional function to check whether the black pixel just found is actually a square and not some artifact
		 */
		for(int i=20; i<imageRaster.getWidth()-20; i++){
			for(int j=20; j<imageRaster.getHeight()-20; j++){
				pixArray = imageRaster.getPixel(i, j, pixArray);
				blackPixel = new Pixel(i, j, pixArray[0]);
				if(blackPixel.compareToPixelFromValue(0)){
					if(checkForSquarePresence(blackPixel, imageRaster)){
						break searchingForBlackPixelLoop;
					}
				}
			}
			
		}
		int pix1Array[] = new int[1];
		pix1Array = imageRaster.getPixel(1, 1, pix1Array);
		
		int pix2Array[] = new int[1];
		pix2Array = imageRaster.getPixel(20, 20, pix2Array);
		
		int pix3Array[] = new int[1];
		pix3Array = imageRaster.getPixel(1000, 1000, pix3Array);
		//blackPixel = new Pixel(blackPixel.getPositionX() + 30, blackPixel.getPositionY() + 30, blackPixel.getValueOfPixel());
		
		
		/*
		 * Find a center of black square 12
		 */
		SquareLocation squareBlack12 = findCenter(blackPixel, imageRaster);
		Location squareBlack12Center = squareBlack12.getGeometricalCenter();
		printMarker(squareBlack12Center, imageRaster, 40, 40);
	

		/*
		 * Find a center of white square 11
		 */
		Pixel pixelWhite11 = moveThroughMultipleEdges(2, EdgeType.TOP, squareBlack12Center, imageRaster);
		SquareLocation squareWhite11 = findCenter(pixelWhite11, imageRaster);
		Location squareWhite11Center = squareWhite11.getGeometricalCenter();
		
		//Current location and pixel of algorithm progression will be put into this field
		Location currentHeadLocation = squareWhite11Center;
		Pixel currentHeadPixel = Pixel.getPixelFromLocation(currentHeadLocation, imageRaster);
		
		//Location&Pixel to help with loop flow
		Location firstLocation=null;
		Pixel firstPixel=null;
		/*
		 * Loop iterating through every square going 1.1-1.2-1.3......-1.14-2.1-2.2-2.3.... etc.
		 */
		for(int i=0; i<10; i++){
			for(int j=0; j<13; j++){
				
				if(j==0){
					firstLocation = currentHeadLocation;
					firstPixel = currentHeadPixel;
				}
				//Pull square data in here
				getSquareColorData(currentHeadLocation,imageRaster);
				
				//Move right - to the next square
				Pixel pixelNextSquare = moveThroughMultipleEdges(2, EdgeType.RIGHT, currentHeadPixel, imageRaster);
				
				//Find the center of the current square and assign HEAD to it 
				SquareLocation nextSquareLocation = findCenter(pixelNextSquare, imageRaster);
				currentHeadPixel = Pixel.getPixelFromLocation(nextSquareLocation.getGeometricalCenter(), imageRaster);
				currentHeadLocation = currentHeadPixel;	
				
			}
			//Pull square data from last square in the current row
			getSquareColorData(currentHeadLocation,imageRaster);
			
			/*
			 * A very shitty workaround to break out of the loop prematurely during the final iteration
			 * In Poland we call this MANIANA
			 * It is done so the algorithm does not attempt to move lower than 10th row (as there is no
			 * such row behaviour would be unpredictable possibly even crashing the program)
			 */
			if(i==9)
			{
				break;
			}
			//Move HEAD back to first square in a row position
			currentHeadLocation=firstLocation;
			currentHeadPixel=firstPixel;
			
			//Move one row down
			Pixel pixelNextSquare = moveThroughMultipleEdges(2, EdgeType.BOTTOM, currentHeadPixel, imageRaster);
			
			//Find the center of the current square and assign HEAD to it 
			SquareLocation nextSquareLocation = findCenter(pixelNextSquare, imageRaster);
			currentHeadPixel = Pixel.getPixelFromLocation(nextSquareLocation.getGeometricalCenter(), imageRaster);
			currentHeadLocation = currentHeadPixel;	
		}
		
		
	}
	/*
	 * Test algorithm - debug only
	 */
	static void mainAlgorithmTest(WritableRaster imageRaster){
		Location location = new Location(800,480);
//		Pixel startPixel = Pixel.getPixelFromLocation(location, imageRaster);
//		Location newEdgeLocation2 = findEdge(startPixel, 30, 30, imageRaster, EdgeType.BOTTOM);
//		Pixel startPixel2 = Pixel.getPixelFromLocation(newEdgeLocation2, imageRaster);
//		Location newEdgeLocation3 = findEdge(startPixel2, 30, 30, imageRaster, EdgeType.TOP);
//		checkSquareAtLocation(location, imageRaster, 30, 30);	
//		checkSquareAtLocation(newEdgeLocation2, imageRaster, 30, 30);	
//		checkSquareAtLocation(newEdgeLocation3, imageRaster, 30, 30);	

		Pixel startPixel = Pixel.getPixelFromLocation(location, imageRaster);
		SquareLocation squareLocationCenter = findCenter(startPixel, imageRaster);
		Location location2 = squareLocationCenter.getGeometricalCenter();
		Location location3 = squareLocationCenter.getLeftEdge();
		Location location4 = squareLocationCenter.getRightEdge();
		Location location5 = squareLocationCenter.getTopEdge();
		Location location6 = squareLocationCenter.getBottomEdge();
		checkSquareAtLocation(location, imageRaster, 30, 30);
		checkSquareAtLocation(location2, imageRaster, 30, 30);
		checkSquareAtLocation(location3, imageRaster, 30, 30);
		checkSquareAtLocation(location4, imageRaster, 30, 30);
		checkSquareAtLocation(location5, imageRaster, 30, 30);
		checkSquareAtLocation(location6, imageRaster, 30, 30);
//		Location newEdgeLocation = findEdge(startPixel, 30, 30, imageRaster,EdgeType.RIGHT);
//		Pixel startPixel2 = Pixel.getPixelFromLocation(newEdgeLocation, imageRaster);
//		Location newEdgeLocation2 = findEdge(startPixel2, 30, 30, imageRaster, EdgeType.RIGHT);
//		checkSquareAtLocation(location, imageRaster, 30, 30);
//		checkSquareAtLocation(newEdgeLocation, imageRaster, 30, 30);
//		checkSquareAtLocation(newEdgeLocation2, imageRaster, 30, 30);
//		int[] pixArray = new int[1];
//		Pixel blackPixel=null;
//		
//		searchingForBlackPixelLoop:
//		/*
//		 * 
//		 * Iterating through every pixel searching for a black one - with values (at least close) to 0.
//		 * Going from left to right on outside loop and top to bottom on inside loop should land me on square (1,2).
//		 * **** ADDED **** Additional function to check whether the black pixel just found is actually a square and not some artifact
//		 */
//		for(int i=20; i<imageRaster.getWidth()-20; i++){
//			for(int j=20; j<imageRaster.getHeight()-20; j++){
//				pixArray = imageRaster.getPixel(i, j, pixArray);
//				blackPixel = new Pixel(i, j, pixArray[0]);
//				if(blackPixel.compareToPixelFromValue(22500)){
//					if(checkForSquarePresence(blackPixel, imageRaster)){
//						break searchingForBlackPixelLoop;
//					}
//				}
//			}
//			
//		}
//		
//		/*
//		 * Find a center of black square 12
//		 */
//		SquareLocation squareBlack12 = findCenter(blackPixel, imageRaster);
//		Location squareBlack12Center = squareBlack12.getGeometricalCenter();
//		printSquarePixelValues(squareBlack12Center, 40, 40, 0, 0, imageRaster);
//		printMarker(squareBlack12Center, imageRaster, 40, 40);
	}
	/*
	 * Gets all the colour data from pixels from the center of the square (size is dataSquareSize x dataSquareSize) and puts it into a list 
	 */
	static List<Integer> getSquareColorData(Location location, WritableRaster imageRaster){
		
		//MOCK
		return new ArrayList<Integer>();
		
	}
	/*
	 * Checking whether or not a chosen pixel is part of actual square by searching for dimensions using lines
	 */
	static boolean checkForSquarePresence(Pixel inputPixel, WritableRaster imageRaster){
			boolean[] stopExecutionFlags = new boolean[8];
			int[] valueX = {0, 1, 1, 1, 0, -1, -1, -1};
			int[] valueY = {-1, -1, 0, 1, 1, 1, 0, -1};
			
			for(int i=0; i<100; i++){
				for(int j=0; j<stopExecutionFlags.length; j++){
					if(!stopExecutionFlags[j]){
						Location currentLocation = new Location(inputPixel.getPositionX()+(valueX[j]*i), inputPixel.getPositionY()+(valueY[j]*i));
						Pixel currentPixel = Pixel.getPixelFromLocation(currentLocation, imageRaster);
//						int currentPixelValue[] = new int[1];
//						currentPixelValue = imageRaster.getPixel(1, 1, currentPixelValue);			//WHAT THE FUCK IS THIS SHIT 
						boolean similarityFlag = inputPixel.comparePixels(currentPixel);
						stopExecutionFlags[j] = !similarityFlag;
						
					}
				}
			}
			for(int i=1; i<8; i++){
				if(!stopExecutionFlags[i] && !stopExecutionFlags[i-1]){
					return true;
				}
			}
			
			
		return false;
	}
	
	/*
	 * A debug tool created to mark the current location on the photo so the programmer knows where the
	 * current location is exactly in relation to the photograph
	 */
	static void printMarker(Location inputLocation, WritableRaster imageRaster, int width, int height) {
		Location startMarkingLocation = new Location(inputLocation.getPositionX()-width/2, inputLocation.getPositionY()-height/2);
		for (int i=0; i<width; i++){
			for (int j=0; j<height; j++){
				int[] pixelValue = new int[1];
				//black pixel
				pixelValue[0]=0;
				imageRaster.setPixel(startMarkingLocation.getPositionX()-width/2+i,
						startMarkingLocation.getPositionY()-height/2+j, pixelValue);
				
			}
		}
	}
	
	static void printSquarePixelValues(Location startPosition, int width, int height, int offsetX, int offsetY, WritableRaster imageRaster){
		int minPixel=65535;
		int maxPixel=0;
		long allPixelsValues=0;
		int numberOfPixels=0;
		for(int i=(startPosition.getPositionX()-(width/2)+offsetX); i<(startPosition.getPositionX()+(width/2)+offsetX); i++){
			for(int j=(startPosition.getPositionY()-(height/2)+offsetY); j<(startPosition.getPositionY()+(height/2)+offsetY); j++){
				int[] pixArray = new int[1];
				pixArray = imageRaster.getPixel(i, j, pixArray);
				if(minPixel>pixArray[0]){
					minPixel=pixArray[0];
				}
				if(maxPixel<pixArray[0]){
					maxPixel=pixArray[0];
				}
				allPixelsValues += pixArray[0];
				numberOfPixels++;
				//System.out.println(pixArray[0]);
			}
		}
		System.out.println("MAX PIXEL = ");
		System.out.println(maxPixel);
		System.out.println("MIN PIXEL = ");
		System.out.println(minPixel);
		System.out.println("AVERAGE VALUE OF PIXELS");
		float averagePixelValue =  allPixelsValues/numberOfPixels;
		System.out.println(averagePixelValue);
	}
	
	static void checkSquareAtLocation(Location location, WritableRaster imageRaster, int width, int height){
		printSquarePixelValues(location, width, height, 0, 0, imageRaster);
		printMarker(location, imageRaster, width, height);
	}
	
	/* Finding center of a square by giving just location of one pixel anywhere
	inside that square and writableraster with entire image
	
	Returns SquareLocation containing center of the square and all 4 edge locations
	*/
	
	static SquareLocation findCenter(Pixel inputPixel, WritableRaster imageRaster) {
		Location locationTop, locationBottom, locationLeft, locationRight;
		locationTop = findEdge(inputPixel, 20, 20, imageRaster, EdgeType.TOP);
		locationBottom = findEdge(inputPixel, 20, 20, imageRaster, EdgeType.BOTTOM);
		locationLeft = findEdge(inputPixel, 20, 20, imageRaster, EdgeType.LEFT);
		locationRight = findEdge(inputPixel, 20, 20, imageRaster, EdgeType.RIGHT);
		
		int coordX, coordY;
		coordX = locationLeft.getPositionX() + ((locationRight.getPositionX()-locationLeft.getPositionX())/2);
		coordY = locationTop.getPositionY() + ((locationBottom.getPositionY()-locationTop.getPositionY())/2);
		Location centerLocation = new Location(coordX, coordY);
		
		SquareLocation squareLocation = new SquareLocation(locationTop, locationBottom, locationLeft, locationRight, centerLocation);

		return squareLocation;
	}
	
	/*Moves through specified number of edges in specified direction (number of edges and direction in input parameters)
	 * */
	static Pixel moveThroughMultipleEdges(int edgeNumber, EdgeType direction, Location startLocation, WritableRaster imageRaster){
		int i=0;
		Pixel startPixel = Pixel.getPixelFromLocation(startLocation, imageRaster);
		while(i<edgeNumber){
			Location afterCurrentEdgeLocaton= moveThroughEdge(startPixel, imageRaster, direction);
			startPixel = Pixel.getPixelFromLocation(afterCurrentEdgeLocaton, imageRaster);
			
		}
		
		return new Pixel();
	}
	
	/*
	 * Moves through just one edge (basically finds an edge and then cuts into it by edgeOffset value
	 */
	
	static Location moveThroughEdge(Pixel inputPixel, WritableRaster imageRaster,
			EdgeType edgeType){
		Location locationOnEdge = findEdge(inputPixel, 20, 20, imageRaster, edgeType);
		int x=0, y=0;
		switch (edgeType) {
		case TOP: {
			x = 0;
			y = -1;
		}
		case BOTTOM: {
			x = 0;
			y = 1;
		}
		case LEFT: {
			x = -1;
			y = 0;
		}
		case RIGHT: {
			x = 1;
			y = 0;
		}
		}
		Location locationThroughEdge = new Location(locationOnEdge.getPositionX()+(x*edgeOffset), locationOnEdge.getPositionY()+(y*edgeOffset));
		return locationThroughEdge;
		
	}
	
	/*
	 * Finds edges comparing squares separate to each other
	 */
	static Location findEdge(Pixel inputPixel, int checkSquareWidth, int checkSquareHeight, WritableRaster imageRaster,
			EdgeType edgeType) {
		int x = 0, y = 0;
		int xSquareCoordinates = 0, ySquareCoordinates = 0;
		int[] pixelArray = new int[1];
		int[] currentPixArrayLower = new int[1], currentPixArrayHigher = new int[1];
		int pixelLowerSquareValue = 0, pixelHigherSquareValue = 0;
		int pixelLowerSquareCount = 0, pixelHigherSquareCount = 0;
		float pixelLowerAverageValue = 0, pixelHigherAverageValue = 0;
		float pixelLowerAverageValueFirst = 0;
		int i = 30;
		
		//Making even values odd
		if(checkSquareWidth%2!=1){
			checkSquareWidth++;
		}
		
		if(checkSquareHeight%2!=1){
			checkSquareHeight++;
		}
		/*
		 * LUT Table : 
		 * TOP -> LEFT
		 * BOTTOM -> RIGHT
		 * RIGHT -> TOP
		 * LEFT -> BOTTOM
		 */
		
		switch (edgeType) {
		case TOP: {
			x = 0;
			y = -1;
			//LEFT FOR SQUARE COORDS
			xSquareCoordinates = -1;
			ySquareCoordinates = 0;
			break;
		}
		case BOTTOM: {
			x = 0;
			y = 1;
			//RIGHT FOR SQUARE COORDS
			xSquareCoordinates = 1;
			ySquareCoordinates = 0;
			break;
		}
		case LEFT: {
			x = -1;
			y = 0;
			//BOTTOM FOR SQUARE COORDS
			xSquareCoordinates = 0;
			ySquareCoordinates = 1;
			break;
		}
		case RIGHT: {
			x = 1;
			y = 0;
			//TOP FOR SQUARE COORDS
			xSquareCoordinates = 0;
			ySquareCoordinates = -1;
			break;
		}
		}
		while (true) {
			i++;
			pixelArray = imageRaster.getPixel(inputPixel.getPositionX()
					+ (i * x), inputPixel.getPositionY() + (i * y), pixelArray);
			
			//Edge determining algorithm using squares and average pixel values from them
			
			int coordXLowerSquare = inputPixel.getPositionX() + (((i-1) * x) - (((int)(checkSquareWidth/2))*x));
			int coordYLowerSquare = inputPixel.getPositionY() + (((i-1) * y) - (((int)(checkSquareHeight/2))*y));
			
			int coordXHigherSquare = inputPixel.getPositionX() + (((i+1) * x) + (((int)(checkSquareWidth/2))*x));
			int coordYHigherSquare = inputPixel.getPositionY() + (((i+1) * y) + (((int)(checkSquareHeight/2))*y));
//			
//			Location edgeOfLowerSquare = new Location(inputPixel.getPositionX()
//					+ ((i-1) * x + (((int)(checkSquareWidth/2))*xSquareCoordinates)), inputPixel.getPositionY()
//					+ ((i-1) * y + (checkSquareHeight*ySquareCoordinates)));
//			
//			Location edgeOfHigherSquare = new Location(inputPixel.getPositionX()
//					+ ((i+1) * x + ((int)(checkSquareWidth/2)*xSquareCoordinates)), inputPixel.getPositionY()
//					+ ((i+1) * y + (checkSquareHeight*ySquareCoordinates)));
			
			Location edgeOfLowerSquareCenter = new Location(coordXLowerSquare, coordYLowerSquare);
			Location edgeOfHigherSquareCenter = new Location(coordXHigherSquare, coordYHigherSquare);
			
			for(int j=0; j<checkSquareWidth; j++){
				for(int k=0; k<checkSquareHeight; k++){
					currentPixArrayHigher = imageRaster.getPixel(coordXHigherSquare-(checkSquareWidth/2)+j,
							coordYHigherSquare-(checkSquareHeight/2)+k,currentPixArrayHigher);
					pixelHigherSquareValue += currentPixArrayHigher[0];
					pixelHigherSquareCount++;
					
					currentPixArrayLower = imageRaster.getPixel(coordXLowerSquare-(checkSquareWidth/2)+j,
							coordYLowerSquare-(checkSquareHeight/2)+k,currentPixArrayLower);
					pixelLowerSquareValue += currentPixArrayLower[0];
					pixelLowerSquareCount++;
				}
			}
			pixelLowerAverageValue = pixelLowerSquareValue/pixelLowerSquareCount;
			pixelHigherAverageValue = pixelHigherSquareValue/pixelHigherSquareCount;
			
			if(i%10==1){
				pixelLowerAverageValueFirst = pixelLowerAverageValue;
			}
			
			
			if (!compareTwoFloats(pixelLowerAverageValueFirst, pixelHigherAverageValue)) {

				break;
			}
			pixelLowerSquareValue=0;
			pixelLowerSquareCount=0;
			pixelHigherSquareValue=0;
			pixelHigherSquareCount=0;
		}
		i--;
		Location locationOfEdge = new Location(inputPixel.getPositionX() + (i * x), inputPixel.getPositionY() + (i * y));

		return locationOfEdge;
	}
	
	/*
	 * Comparing 2 separate values with offset
	 */
	static boolean compareTwoFloats(float Integer1, float Integer2){
		//System.out.println(maxValueOfPixel*offsetValue);
		if((Integer1>(Integer2-(Pixel.maxValueOfPixel*Pixel.offsetValueForCheck))) && (Integer1<(Integer2+(Pixel.maxValueOfPixel*Pixel.offsetValueForCheck)))){
			return true;
		}
		return false;
	}

}
