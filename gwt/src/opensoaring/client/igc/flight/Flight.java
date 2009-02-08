package opensoaring.client.igc.flight;

import java.util.ArrayList;

import opensoaring.client.igc.LogParser;

/**
 * Represents an IGC flight log, including its raw and parsed data.
 * 
 * @author Ricardo Rocha <rocha@opensoaring.info>
 *
 */
public class Flight {

	/**
	 * A string containing the IGC raw data.
	 */
	private String igcData;
	
	/**
	 * An object containing the multiple properties of this flight, parsed from the raw IGC log.
	 */
	private FlightProperties flightProps;
	
	/**
	 * The list of Fix objects parsed from the IGC flight log.
	 */
	private ArrayList<Fix> flightFixes = new ArrayList<Fix>();
	
	/**
	 * Class constructor.
	 */
	public Flight() {
		this(null);
	}
	
	/**
	 * Class constructor.
	 * 
	 * @param igcData A String containing the raw IGC flight log data
	 */
	public Flight(String igcData) {
		this.igcData = igcData;
		this.flightProps = new FlightProperties();
	}
	
	/**
	 * Returns the raw IGC flight log data.
	 * 
	 * @return The raw IGC flight log data
	 */
	public String getIgcData() {
		return igcData;
	}
	
	/**
	 * Sets the raw IGC flight log data of the object.
	 * 
	 * @param igcData The raw IGC flight log data
	 */
	public void setIgcData(String igcData) {
		this.igcData = igcData;
	}
	
	/**
	 * Returns the object containing the properties of the Flight (parsed from the IGC log).
	 * 
	 * @return The object containing the properties of the Flight (parsed from the IGC log).
	 */
	public FlightProperties getFlightProps() {
		return flightProps;
	}

	/**
	 * Sets the properties of the flight (parsed from the IGC log).
	 * 
	 * @param flightProps The properties of the flight (parsed from the IGC log).
	 */
	public void setFlightProps(FlightProperties flightProps) {
		this.flightProps = flightProps;
	}

	/**
	 * Returns the list of Fix objects of the Flight (taken from the IGC log data).
	 * 
	 * @return The list of Fix objects of the Flight (taken from the IGC log data)
	 */
	public ArrayList<Fix> getFlightFixes() {
		return flightFixes;
	}

	/**
	 * Sets the list of Fix objects taken from the IGC log data.
	 * 
	 * @param flightFixes The list of Fix objects taken from the IGC log data.
	 */
	public void setFlightFixes(ArrayList<Fix> flightFixes) {
		this.flightFixes = flightFixes;
	}

	/**
	 * Parses the data in this IGC Flight log.
	 */
	public void parse() {
		String[] records = igcData.split("\n");
		
		for (String record: records) {
			char recordType = record.charAt(0);
			switch(recordType) {
			case 'A':
				LogParser.parseRecorderInfo(record, flightProps);
				break;
			case 'B':
				flightFixes.add(LogParser.parseFix(record, flightProps));
				break;
			case 'C':
				LogParser.parseFlightDeclaration(record, flightProps);
			case 'H':
				LogParser.parseHeaderInfo(record, flightProps);
				break;
			case 'I':
				LogParser.parseFixExtensions(record, flightProps);
				break;
			}
		}
	}
		
}
