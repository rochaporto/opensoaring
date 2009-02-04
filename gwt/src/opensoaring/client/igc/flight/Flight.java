package opensoaring.client.igc.flight;

import java.util.ArrayList;

import opensoaring.client.igc.LogParser;

public class Flight {

	private String igcData;
	
	private FlightProperties flightProps;
	
	private ArrayList<Fix> flightFixes = new ArrayList<Fix>();
	
	public Flight() {
		this(null);
	}
	
	public Flight(String igcData) {
		this.igcData = igcData;
		this.flightProps = new FlightProperties();
	}
	
	public String getIgcData() {
		return igcData;
	}
	
	public void setIgcData(String igcData) {
		this.igcData = igcData;
	}
	
	/**
	 * @return the flightProps
	 */
	public FlightProperties getFlightProps() {
		return flightProps;
	}

	/**
	 * @param flightProps the flightProps to set
	 */
	public void setFlightProps(FlightProperties flightProps) {
		this.flightProps = flightProps;
	}

	/**
	 * @return the flightFixes
	 */
	public ArrayList<Fix> getFlightFixes() {
		return flightFixes;
	}

	/**
	 * @param flightFixes the flightFixes to set
	 */
	public void setFlightFixes(ArrayList<Fix> flightFixes) {
		this.flightFixes = flightFixes;
	}

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
			case 'H':
				LogParser.parseHeaderInfo(record, flightProps);
				break;
			case 'I':
				LogParser.parseFixExtensions(record, flightProps);
				break;
			}
		}
		
		//Window.alert(flightProps + " :: " + flightFixes.size());
	}
	
}
