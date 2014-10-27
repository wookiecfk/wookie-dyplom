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
		 * Iterating through every pixel searching for a black one - with values (at leats close) to 0.
		 * Going from left to right on outside loop and top to bottom on inside loop should land me on square (1,2).
		 */
		for(int i=0; i<imageRaster.getWidth(); i++){
			for(int j=0; j<imageRaster.getHeight(); j++){
				pixArray = imageRaster.getPixel(i, j, pixArray);
				blackPixel = new Pixel(i, j, pixArray[0]);
				if(blackPixel.compareToPixelFromValue(0)){
					break searchingForBlackPixelLoop;
				}
			}
			
		}
		/*
		 * Find a center of black square 12
		 */
		SquareLocation squareBlack12 = findCenter(blackPixel, imageRaster);
		Location squareBlack12Center = squareBlack12.getGeometricalCenter();
		
		/*
		 * Find a center of white square 11
		 */
		Pixel pixelWhite11 = moveThroughMultipleEdges(2, EdgeType.TOP, squareBlack12Center, imageRaster);
		SquareLocation squareWhite11 = findCenter(pixelWhite11, imageRaster);
		Location squareWhite11Center = squareWhite11.getGeometricalCenter();
		
		
	}
	/*
	 * Gets all the colour data from pixels from the center of the square (size is dataSquareSize x dataSquareSize) and puts it into a list 
	 */
	static List<Integer> getSquareColorData(Location location, WritableRaster imageRaster){
		
		//MOCK
		return new ArrayList<Integer>();
		
	}
	/* Finding center of a square by giving just location of one pixel anywhere
	inside that square and writableraster with entire image
	
	Returns SquareLocation containing center of the square and all 4 edge locations
	*/
	static SquareLocation findCenter(Pixel inputPixel, WritableRaster imageRaster) {
		Location locationTop, locationBottom, locationLeft, locationRight;
		locationTop = findEdge(inputPixel, imageRaster, EdgeType.TOP);
		locationBottom = findEdge(inputPixel, imageRaster, EdgeType.BOTTOM);
		locationLeft = findEdge(inputPixel, imageRaster, EdgeType.LEFT);
		locationRight = findEdge(inputPixel, imageRaster, EdgeType.RIGHT);
		
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
		Location locationOnEdge = findEdge(inputPixel, imageRaster, edgeType);
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

	static Location findEdge(Pixel inputPixel, WritableRaster imageRaster,
			EdgeType edgeType) {
		int x = 0, y = 0;
		int[] pixelArray = new int[1];
		int i = 0;
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
		while (true) {
			i++;
			pixelArray = imageRaster.getPixel(inputPixel.getPositionX()
					+ (i * x), inputPixel.getPositionY() + (i * y), pixelArray);
			if (!inputPixel.compareToPixelFromValue(pixelArray[0])) {

				break;
			}
		}
		i--;
		Location locationOfEdge = new Location(inputPixel.getPositionX() + (i * x), inputPixel.getPositionY() + (i * y));

		return locationOfEdge;
	}

}
