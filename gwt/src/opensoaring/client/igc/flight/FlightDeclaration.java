package opensoaring.client.igc.flight;

import java.util.ArrayList;
import java.util.Date;

public class FlightDeclaration {

	/**
	 * The date at which the declaration was made.
	 */
	private Date date;
	
	/**
	 * The date of the actual flight which performed the declared flight.
	 */
	private Date flightDate;
	
	/**
	 * The ID of the task within a day.
	 */
	private int taskNumber;
	
	/**
	 * The total number of turn points in the flight declaration.
	 */
	private int numberTurnpoints;
	
	/**
	 * The declared takeoff point. 
	 */
	private Fix takeoff;
	
	/**
	 * The declared start point.
	 */
	private Fix start;
	
	/**
	 * The declared set of turn points.
	 */
	private ArrayList<Fix> turnPoints;
	
	/**
	 * The declared finish point.
	 */
	private Fix finish;
	
	/**
	 * The declared landing point.
	 */
	private Fix landing;
	
	/**
	 * Class constructor.
	 */
	public FlightDeclaration() {
		this(null, null, new ArrayList<Fix>(), null, null);
	}
	
	/**
	 * Class constructor.
	 * 
	 * @param takeoff The takeoff point of the declaration
	 * @param start The start point of the declaration
	 * @param turnPoints The list of turn points in the declaration
	 * @param finish The finish point of the declaration
	 * @param landing The landing point of the declaration
	 */
	public FlightDeclaration(Fix takeoff, Fix start, ArrayList<Fix> turnPoints, 
			Fix finish, Fix landing) {
		this.takeoff = takeoff;
		this.start = start;
		this.turnPoints = turnPoints;
		this.finish = finish;
		this.landing = landing;
	}

	/**
	 * Returns the date of the declaration.
	 * 
	 * @return The date of the declaration
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date of the declaration.
	 * 
	 * @param date The date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Returns the date of the actual flight.
	 * 
	 * @return The date of the actual flight 
	 */
	public Date getFlightDate() {
		return flightDate;
	}

	/**
	 * Sets the date of the actual flight.
	 * 
	 * @param flightDate The date of the actual flight to set 
	 */
	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}

	/**
	 * Returns the ID of the task within the day.
	 * 
	 * @return The ID of the task within the day
	 */
	public int getTaskNumber() {
		return taskNumber;
	}

	/**
	 * Sets the ID of the task within the day.
	 * 
	 * @param taskNumber The ID of the task within the day
	 */
	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	/**
	 * Returns the number of turn points of the declared task.
	 * 
	 * @return The number of turn points of the declared task
	 */
	public int getNumberTurnpoints() {
		return numberTurnpoints;
	}

	/**
	 * Sets the number of turn points of the declared task.
	 * 
	 * @param numberTurnpoints The number of turnpoints to set
	 */
	public void setNumberTurnpoints(int numberTurnpoints) {
		this.numberTurnpoints = numberTurnpoints;
	}

	/**
	 * Returns the declared takeoff point.
	 * 
	 * @return The declared takeoff point
	 */
	public Fix getTakeoff() {
		return takeoff;
	}

	/**
	 * Sets the declared takeoff point.
	 * 
	 * @param takeoff The takeoff point to set
	 */
	public void setTakeoff(Fix takeoff) {
		this.takeoff = takeoff;
	}

	/**
	 * Returns the declared start point.
	 * 
	 * @return The declared start point
	 */
	public Fix getStart() {
		return start;
	}

	/**
	 * Sets the declared start point.
	 * 
	 * @param start The declared start point to set
	 */
	public void setStart(Fix start) {
		this.start = start;
	}

	/**
	 * Returns the list of declared turn point.
	 * 
	 * @return The list of declared turn points
	 */
	public ArrayList<Fix> getTurnPoints() {
		return turnPoints;
	}

	/**
	 * Sets the list of declared turn points.
	 * 
	 * @param turnPoints The list of declared turn points to set
	 */
	public void setTurnPoints(ArrayList<Fix> turnPoints) {
		this.turnPoints = turnPoints;
	}

	/**
	 * Returns the declared finish point.
	 * 
	 * @return The declared finish point
	 */
	public Fix getFinish() {
		return finish;
	}

	/**
	 * Sets the declared finish point.
	 * 
	 * @param finish The declared finish point to set
	 */
	public void setFinish(Fix finish) {
		this.finish = finish;
	}

	/**
	 * Returns the declared landing point.
	 * 
	 * @return The declared landing point
	 */
	public Fix getLanding() {
		return landing;
	}

	/**
	 * Sets the declared landing point.
	 * 
	 * @param landing The landing point to set
	 */
	public void setLanding(Fix landing) {
		this.landing = landing;
	}
	
}
