package opensoaring.client.igc.flight;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the set of properties taken from an IGC flight log.
 * 
 * @author Ricardo Rocha <rocha@opensoaring.info>
 *
 */
public class FlightProperties {

	/**
	 * The brand of the recorder generating the IGC flight log.
	 */
	private String recorderManufacturer;
	
	/**
	 * The ID (serial number) of the recorder manufacturer.
	 */
	private String recorderId;
	
	/**
	 * A String containing additional information regarding the recorder.
	 */
	private String recorderIdExtension;
	
	/**
	 * The date of the flight.
	 */
	private Date flightDate;
	
	/**
	 * The Fix accuracy, in the form of estimated position error.
	 */
	private String fixAccuracy;
	
	/**
	 * The name of the pilot in command.
	 */
	private String pilotInCharge;
	
	/**
	 * The name of the second person in the flight (if any).
	 */
	private String crewMember2;
	
	/**
	 * The type (brand, model) of the glider.
	 */
	private String gliderType;
	
	/**
	 * The glider registration.
	 */
	private String gliderId;
	
	/**
	 * A list of extensions to the mandatory fix information.
	 */
	private ArrayList<FixExtension> fixExtensions = new ArrayList<FixExtension>();

	/**
	 * The flight declaration (if any).
	 * 
	 * @see FlightDeclaration
	 */
	private FlightDeclaration flightDeclaration;
	
	/**
	 * Class constructor.
	 */
	public FlightProperties() {
		
	}
	
	/**
	 * Returns the list of Fix extensions available in the IGC flight log.
	 * 
	 * @return A list of Fix extensions available in the IGC flight log.
	 */
	public ArrayList<FixExtension> getFixExtensions() {
		return fixExtensions;
	}

	/**
	 * Sets the list of available Fix extensions in the flight log.
	 * 
	 * @param fixExtensions The list of available Fix extensions in the flight log
	 */
	public void setFixExtensions(ArrayList<FixExtension> fixExtensions) {
		this.fixExtensions = fixExtensions;
	}
	
	/**
	 * Adds a new Fix extension to the object.
	 * 
	 * @param fixExtension The Fix extension to add
	 */
	public void addFixExtension(FixExtension fixExtension) {
		this.fixExtensions.add(fixExtension);
	}

	/**
	 * Returns the Fix accuracy of the data in the flight log.
	 * 
	 * @return The Fix accuracy of the data in the log
	 */
	public String getFixAccuracy() {
		return fixAccuracy;
	}

	/**
	 * Sets the Fix accuracy of the data in the flight log.
	 * 
	 * @param fixAccuracy The Fix accuracy to set
	 */
	public void setFixAccuracy(String fixAccuracy) {
		this.fixAccuracy = fixAccuracy;
	}

	/**
	 * Returns the name of the pilot in charge.
	 * 
	 * @return The name of the pilot in charge
	 */
	public String getPilotInCharge() {
		return pilotInCharge;
	}

	/**
	 * Sets the name of the pilot in charge.
	 * 
	 * @param pilotInCharge The name to set as pilot in charge
	 */
	public void setPilotInCharge(String pilotInCharge) {
		this.pilotInCharge = pilotInCharge;
	}

	/**
	 * Returns the name of the second crew member (if any).
	 * 
	 * @return The name of the second crew member (if any)
	 */
	public String getCrewMember2() {
		return crewMember2;
	}

	/**
	 * Sets the name of the second crew member.
	 * 
	 * @param crewMember2 The name to set as second crew member
	 */
	public void setCrewMember2(String crewMember2) {
		this.crewMember2 = crewMember2;
	}

	/**
	 * Returns the type (brand, name) of the glider.
	 * 
	 * @return The type of glider
	 */
	public String getGliderType() {
		return gliderType;
	}

	/**
	 * Sets the type of glider (brand, name).
	 * 
	 * @param gliderType The glider type to set
	 */
	public void setGliderType(String gliderType) {
		this.gliderType = gliderType;
	}

	/**
	 * Returns the ID (registration) of the glider.
	 * 
	 * @return The ID (registration) of the glider
	 */
	public String getGliderId() {
		return gliderId;
	}

	/**
	 * Sets the ID (registration) of the glider.
	 * 
	 * @param gliderId The ID (registration) to set
	 */
	public void setGliderId(String gliderId) {
		this.gliderId = gliderId;
	}

	/**
	 * Returns the name of the recorder manufacturer.
	 * 
	 * @return The name of the recorder manufacturer
	 */
	public String getRecorderManufacturer() {
		return recorderManufacturer;
	}

	/**
	 * Sets the name of the recorder manufacturer.
	 * 
	 * @param recorderManufacturer The recorder manufacturer to set
	 */
	public void setRecorderManufacturer(String recorderManufacturer) {
		this.recorderManufacturer = recorderManufacturer;
	}

	/**
	 * Returns the ID of the recorder manufacturer.
	 * 
	 * @return The ID of the recorder manufacturer
	 */
	public String getRecorderId() {
		return recorderId;
	}

	/**
	 * Sets the ID of the recorder manufacturer.
	 * 
	 * @param recorderId The ID of the recorder manufacturer
	 */
	public void setRecorderId(String recorderId) {
		this.recorderId = recorderId;
	}

	/**
	 * Returns a String with additional information regarding the recorder manufacturer.
	 * 
	 * @return Additional information regarding the recorder manufacturer
	 */
	public String getRecorderIdExtension() {
		return recorderIdExtension;
	}

	/**
	 * Sets additional information regarding the recorder manufacturer.
	 * 
	 * @param recorderIdExtension Additional information regarding the recorder manufacturer
	 */
	public void setRecorderIdExtension(String recorderIdExtension) {
		this.recorderIdExtension = recorderIdExtension;
	}

	/**
	 * Returns the date of the flight.
	 * 
	 * @return The flight date
	 */
	public Date getFlightDate() {
		return flightDate;
	}

	/**
	 * Sets the date of the flight.
	 * 
	 * @param flightDate The flight date to set
	 */
	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}
	
	/**
	 * Returns the object containing the flight declaration.
	 * 
	 * @return The FlightDeclaration object
	 */
	public FlightDeclaration getFlightDeclaration() {
		return flightDeclaration;
	}

	/**
	 * Sets the object containing the flight declaration.
	 * 
	 * @param flightDeclaration The FlightDeclaration to set
	 */
	public void setFlightDeclaration(FlightDeclaration flightDeclaration) {
		this.flightDeclaration = flightDeclaration;
	}
	
}
