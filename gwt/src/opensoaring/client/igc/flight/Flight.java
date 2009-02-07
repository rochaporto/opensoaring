package opensoaring.client.igc.flight;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import opensoaring.client.igc.LogParser;
import opensoaring.client.igc.LogUtil;

public class Flight {

	private double maxSpeed = 350;
	
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

	/**
	 * @return the maxSpeed
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
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
	
	public void validate() {
		for (int i=1; i<flightFixes.size(); i++) {
			double speed = LogUtil.groundSpeed(flightFixes.get(i-1), flightFixes.get(i));
			if (speed > maxSpeed) {
				flightFixes.remove(i);
				++i;
			}
		}
	}
	
	public void parseAndValidate() {
		parse();
		validate();
	}
	
}
