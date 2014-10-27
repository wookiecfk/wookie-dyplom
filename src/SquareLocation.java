
public class SquareLocation {
	Location mTopEdge;
	Location mBottomEdge;
	Location mLeftEdge;
	Location mRightEdge;
	Location mGeometricalCenter;
	
	protected SquareLocation(Location mTopEdge, Location mBottomEdge,	Location mLeftEdge, 
			Location mRightEdge, Location mGeometricalCenter) {
		
		this.mTopEdge = mTopEdge;
		this.mBottomEdge = mBottomEdge;
		this.mLeftEdge = mLeftEdge;
		this.mRightEdge = mRightEdge;
		this.mGeometricalCenter = mGeometricalCenter;
	}

	public Location getTopEdge() {
		return mTopEdge;
	}

	public void setTopEdge(Location mTopEdge) {
		this.mTopEdge = mTopEdge;
	}

	public Location getBottomEdge() {
		return mBottomEdge;
	}

	public void setBottomEdge(Location mBottomEdge) {
		this.mBottomEdge = mBottomEdge;
	}

	public Location getLeftEdge() {
		return mLeftEdge;
	}

	public void setLeftEdge(Location mLeftEdge) {
		this.mLeftEdge = mLeftEdge;
	}

	public Location getRightEdge() {
		return mRightEdge;
	}

	public void setRightEdge(Location mRightEdge) {
		this.mRightEdge = mRightEdge;
	}

	public Location getGeometricalCenter() {
		return mGeometricalCenter;
	}

	public void setGeometricalCenter(Location mGeometricalCenter) {
		this.mGeometricalCenter = mGeometricalCenter;
	}
}
