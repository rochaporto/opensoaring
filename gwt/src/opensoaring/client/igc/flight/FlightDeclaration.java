package opensoaring.client.igc.flight;

import java.util.ArrayList;
import java.util.Date;

public class FlightDeclaration {

	private Date date;
	
	private Date flightDate;
	
	private int taskNumber;
	
	private int numberTurnpoints;
	
	private Fix takeoff;
	
	private Fix start;
	
	private ArrayList<Fix> turnPoints;
	
	private Fix finish;
	
	private Fix landing;
	
	public FlightDeclaration() {
		this(null, null, new ArrayList<Fix>(), null, null);
	}
	
	public FlightDeclaration(Fix takeoff, Fix start, ArrayList<Fix> turnPoints, 
			Fix finish, Fix landing) {
		this.takeoff = takeoff;
		this.start = start;
		this.turnPoints = turnPoints;
		this.finish = finish;
		this.landing = landing;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the flightDate
	 */
	public Date getFlightDate() {
		return flightDate;
	}

	/**
	 * @param flightDate the flightDate to set
	 */
	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}

	/**
	 * @return the taskNumber
	 */
	public int getTaskNumber() {
		return taskNumber;
	}

	/**
	 * @param taskNumber the taskNumber to set
	 */
	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	/**
	 * @return the numberTurnpoints
	 */
	public int getNumberTurnpoints() {
		return numberTurnpoints;
	}

	/**
	 * @param numberTurnpoints the numberTurnpoints to set
	 */
	public void setNumberTurnpoints(int numberTurnpoints) {
		this.numberTurnpoints = numberTurnpoints;
	}

	/**
	 * @return the takeoff
	 */
	public Fix getTakeoff() {
		return takeoff;
	}

	/**
	 * @param takeoff the takeoff to set
	 */
	public void setTakeoff(Fix takeoff) {
		this.takeoff = takeoff;
	}

	/**
	 * @return the start
	 */
	public Fix getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Fix start) {
		this.start = start;
	}

	/**
	 * @return the turnPoints
	 */
	public ArrayList<Fix> getTurnPoints() {
		return turnPoints;
	}

	/**
	 * @param turnPoints the turnPoints to set
	 */
	public void setTurnPoints(ArrayList<Fix> turnPoints) {
		this.turnPoints = turnPoints;
	}

	/**
	 * @return the finish
	 */
	public Fix getFinish() {
		return finish;
	}

	/**
	 * @param finish the finish to set
	 */
	public void setFinish(Fix finish) {
		this.finish = finish;
	}

	/**
	 * @return the landing
	 */
	public Fix getLanding() {
		return landing;
	}

	/**
	 * @param landing the landing to set
	 */
	public void setLanding(Fix landing) {
		this.landing = landing;
	}
	
}
