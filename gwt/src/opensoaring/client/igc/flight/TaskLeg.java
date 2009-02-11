package opensoaring.client.igc.flight;

public class TaskLeg {

	private int startIndex;
	
	private int endIndex;
	
	private double distance;
	
	public TaskLeg() {
		
	}
	
	public TaskLeg(int startIndex, int endIndex, double distance) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.distance = distance;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the endIndex
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @param endIndex the endIndex to set
	 */
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
}
