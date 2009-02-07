package opensoaring.client.igc.flight;

import java.util.ArrayList;
import java.util.Date;

public class FlightProperties {

	private String recorderManufacturer;
	
	private String recorderId;
	
	private String recorderIdExtension;
	
	private Date flightDate;
	
	private String fixAccuracy;
	
	private String pilotInCharge;
	
	private String crewMember2;
	
	private String gliderType;
	
	private String gliderId;
	
	private ArrayList<FixExtension> fixExtensions = new ArrayList<FixExtension>();

	private FlightDeclaration flightDeclaration;
	
	public FlightProperties() {
		
	}
	
	/**
	 * @return the fixExtensions
	 */
	public ArrayList<FixExtension> getFixExtensions() {
		return fixExtensions;
	}

	/**
	 * @param fixExtensions the fixExtensions to set
	 */
	public void setFixExtensions(ArrayList<FixExtension> fixExtensions) {
		this.fixExtensions = fixExtensions;
	}
	
	/**
	 * @param fixExtension the fixExtension to add
	 */
	public void addFixExtension(FixExtension fixExtension) {
		this.fixExtensions.add(fixExtension);
	}

	/**
	 * @return the fixAccuracy
	 */
	public String getFixAccuracy() {
		return fixAccuracy;
	}

	/**
	 * @param fixAccuracy the fixAccuracy to set
	 */
	public void setFixAccuracy(String fixAccuracy) {
		this.fixAccuracy = fixAccuracy;
	}

	/**
	 * @return the pilotInCharge
	 */
	public String getPilotInCharge() {
		return pilotInCharge;
	}

	/**
	 * @param pilotInCharge the pilotInCharge to set
	 */
	public void setPilotInCharge(String pilotInCharge) {
		this.pilotInCharge = pilotInCharge;
	}

	/**
	 * @return the crewMember2
	 */
	public String getCrewMember2() {
		return crewMember2;
	}

	/**
	 * @param crewMember2 the crewMember2 to set
	 */
	public void setCrewMember2(String crewMember2) {
		this.crewMember2 = crewMember2;
	}

	/**
	 * @return the gliderType
	 */
	public String getGliderType() {
		return gliderType;
	}

	/**
	 * @param gliderType the gliderType to set
	 */
	public void setGliderType(String gliderType) {
		this.gliderType = gliderType;
	}

	/**
	 * @return the gliderId
	 */
	public String getGliderId() {
		return gliderId;
	}

	/**
	 * @param gliderId the gliderId to set
	 */
	public void setGliderId(String gliderId) {
		this.gliderId = gliderId;
	}

	/**
	 * @return the recorderManufacturer
	 */
	public String getRecorderManufacturer() {
		return recorderManufacturer;
	}

	/**
	 * @param recorderManufacturer the recorderManufacturer to set
	 */
	public void setRecorderManufacturer(String recorderManufacturer) {
		this.recorderManufacturer = recorderManufacturer;
	}

	/**
	 * @return the recorderId
	 */
	public String getRecorderId() {
		return recorderId;
	}

	/**
	 * @param recorderId the recorderId to set
	 */
	public void setRecorderId(String recorderId) {
		this.recorderId = recorderId;
	}

	/**
	 * @return the recorderIdExtension
	 */
	public String getRecorderIdExtension() {
		return recorderIdExtension;
	}

	/**
	 * @param recorderIdExtension the recorderIdExtension to set
	 */
	public void setRecorderIdExtension(String recorderIdExtension) {
		this.recorderIdExtension = recorderIdExtension;
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
	 * @return the flightDeclaration
	 */
	public FlightDeclaration getFlightDeclaration() {
		return flightDeclaration;
	}

	/**
	 * @param flightDeclaration the flightDeclaration to set
	 */
	public void setFlightDeclaration(FlightDeclaration flightDeclaration) {
		this.flightDeclaration = flightDeclaration;
	}

	public String toString() {
		return "MAN :: " + recorderManufacturer
			+ " :: UID :: " + recorderId
			+ " :: EXT :: " + recorderIdExtension
			+ " :: DTE :: " + flightDate
			+ " :: FXA :: " + fixAccuracy
			+ " :: PLT :: " + pilotInCharge
			+ " :: CM2 :: " + crewMember2
			+ " :: GTY :: " + gliderType
			+ " :: GID :: " + gliderId
			+ " :: FIX EXTS :: " + fixExtensions;
	}
}
