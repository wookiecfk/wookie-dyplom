
public class Location {
	int mPositionX;
	int mPositionY;
	
	public int getPositionX() {
		return mPositionX;
	}

	public void setPositionX(int mPositionX) {
		this.mPositionX = mPositionX;
	}

	public int getPositionY() {
		return mPositionY;
	}

	public void setPositionY(int mPositionY) {
		this.mPositionY = mPositionY;
	}

	protected Location(int mPositionX, int mPositionY) {
		this.mPositionX = mPositionX;
		this.mPositionY = mPositionY;
	}
	
	protected Location(){
		
	}
}
