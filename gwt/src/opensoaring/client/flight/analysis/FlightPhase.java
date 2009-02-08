package opensoaring.client.flight.analysis;

public class FlightPhase {

	public static enum PhaseType {
		CIRCLE, STRAIGHT, TOW
	}
	
	private PhaseType type;
	
	private int start;
	
	private int end;
	
	public FlightPhase() {
		
	}
	
	public FlightPhase(PhaseType type, int start, int end) {
		this.type = type;
		this.start = start;
		this.end = end;		
	}

	/**
	 * @return the type
	 */
	public PhaseType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(PhaseType type) {
		this.type = type;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	
}
