package opensoaring.client.igc.flight;

import java.util.Date;
import java.util.HashMap;

import opensoaring.client.igc.LogParser.TLC;

/**
 * Represents a fix (single point) in an IGC flight log.
 * 
 * All data included is coming from the log file.
 * 
 * @author Ricardo Rocha <ricardo.rocha@cern.ch>
 * 
 */
public class Fix {
	
	/**
	 * 'A' represents a 3D fix, 'V' a 2D fix (no GPS altitude or no GPS data).
	 */
	public static enum FixValidity { A, V };
	
	/**
	 * The time at which this read was done.
	 */
	private Date time;
	
	/**
	 * The latitude of this Fix.
	 */
	private double latitude;
	
	/**
	 * The longitude of this Fix.
	 */
	private double longitude;
	
	/**
	 * Tells if data refers a 2D or 3D Fix read.
	 */
	private FixValidity fixValidity;
	
	/**
	 * Altitude in respect to the ISA sea level datum of 1013.25 HPa.
	 */
	private int pressureAltitude;
	
	/**
	 * Altitude calculated solely using GNSS position lines (GPS data).
	 */
	private int gnssAltitude;
	
	/**
	 * A list of available Fix extension data in the flight log (Three Letter Code to String map).
	 */
	private HashMap<TLC,String> extensions = new HashMap<TLC,String>();
	
	/**
	 * Class constructor.
	 */
	public Fix() {
		
	}
	
	/**
	 * Class constructor.
	 * 
	 * @param latitude The latitude of the Fix
	 * @param longitude The longitude of the Fix
	 * @param gnssAltitude The GNSS (GPS) altitude of the Fix
	 */
	public Fix(double latitude, double longitude, int gnssAltitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.gnssAltitude = gnssAltitude;
	}
	
	/**
	 * Class constructor.
	 * 
	 * @param time The time at which the Fix was recorded
	 * @param latitude The latitude of the Fix
	 * @param longitude The longitude of the Fix
	 * @param fixValidity The validity of the Fix
	 * @param pressureAltitude The altitude taking the ISA sea level pressure into account
	 * @param gnssAltitude The altitude calculated using GNSS (GPS) means only
	 * @see FixValidity
	 */
	public Fix(Date time, double latitude, double longitude, FixValidity fixValidity,
			int pressureAltitude, int gnssAltitude) {
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fixValidity = fixValidity;
		this.pressureAltitude = pressureAltitude;
		this.gnssAltitude = gnssAltitude;		
	}
	
	/**
	 * Returns the list of available Fix extensions (Three Level Code to String mapping).
	 * 
	 * @return The list of available Fix extensions
	 */
	public HashMap<TLC, String> getExtensions() {
		return extensions;
	}

	/**
	 * Sets the available list of extensions in the Fix.
	 * 
	 * @param extensions A Three Letter Code to String mapping describing the extensions
	 */
	public void setExtensions(HashMap<TLC, String> extensions) {
		this.extensions = extensions;
	}
	
	/**
	 * Adds a new extension value to the Fix.
	 * 
	 * @param extension The Three Letter Code of the extension being added
	 * @param value The value of the extension
	 */
	public void addExtension(TLC extension, String value) {
		extensions.put(extension, value);
	}
	
	/**
	 * Retrieves the value of the requested extension (from a Three Letter Code).
	 * 
	 * @param extension The Three Letter Code of the extension to retrieve
	 * @return The value of the extension
	 */
	public String getExtension(TLC extension) {
		if (extensions.keySet().contains(extension)) {
			return extensions.get(extension);
		}
		return null;
	}

	/**
	 * Returns the time at which the Fix was recorded.
	 * 
	 * @return The time at which the Fix was recorded
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Sets the time of the Fix.
	 * 
	 * @param time The time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * Returns the latitude of the Fix.
	 * 
	 * @return The latitude of the Fix
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Returns the latitude of thes Fix (in radians).
	 * 
	 * @return The latitude (in radians of the Fix).
	 */
	public double getLatitudeRadians() {
		return Math.toRadians(latitude);
	}
	
	/**
	 * Sets the latitude of the Fix.
	 * 
	 * @param latitude The latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * Returns the longitude of the Fix.
	 * 
	 * @return The longitude of the Fix
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Returns the longitude of the Fix (in radians).
	 * 
	 * @return The longitude (in radians) of the Fix.
	 */
	public double getLongitudeRadians() {
		return Math.toRadians(longitude);
	}

	/**
	 * Sets the longitude of the Fix.
	 * 
	 * @param longitude The longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * The validity of the Fix.
	 * 
	 * @return The validity of the Fix
	 * @see FixValidity
	 */
	public FixValidity getFixValidity() {
		return fixValidity;
	}

	/**
	 * Sets the validity of the Fix.
	 * 
	 * @param fixValidity The Fix validity to set
	 * @see FixValidity
	 */
	public void setFixValidity(FixValidity fixValidity) {
		this.fixValidity = fixValidity;
	}

	/**
	 * Returns the pressure altitude of the Fix 
	 * (in relation to the ISA sea level datum - 1013.25HPa).
	 * 
	 * @return The pressure altitude of the Fix
	 */
	public int getPressureAltitude() {
		return pressureAltitude;
	}

	/**
	 * Sets the pressure altitude of the Fix.
	 * 
	 * @param pressureAltitude The pressure altitude to set
	 */
	public void setPressureAltitude(int pressureAltitude) {
		this.pressureAltitude = pressureAltitude;
	}

	/**
	 * Returns the GNSS (GPS) altitude of the Fix.
	 * 
	 * @return The GNSS (GPS) altitude of the Fix
	 */
	public int getGnssAltitude() {
		return gnssAltitude;
	}

	/**
	 * Sets the GNSS (GPS) altitude of the Fix.
	 * 
	 * @param gnssAltitude The GNSS (GPS) altitude to set
	 */
	public void setGnssAltitude(int gnssAltitude) {
		this.gnssAltitude = gnssAltitude;
	}
}
