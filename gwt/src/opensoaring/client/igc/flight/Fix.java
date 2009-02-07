package opensoaring.client.igc.flight;

import java.util.Date;
import java.util.HashMap;

import opensoaring.client.igc.LogParser.TLC;

public class Fix {
	
	public static enum FixValidity { A, V };
	
	private Date time;
	
	private double latitude;
	
	private double longitude;
	
	private FixValidity fixValidity;
	
	private int pressureAltitude;
	
	private int gnssAltitude;
	
	private HashMap<TLC,String> extensions = new HashMap<TLC,String>();
	
	public Fix() {
		
	}
	
	public Fix(double latitude, double longitude, int gnssAltitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.gnssAltitude = gnssAltitude;
	}
	
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
	 * @return the extensions
	 */
	public HashMap<TLC, String> getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(HashMap<TLC, String> extensions) {
		this.extensions = extensions;
	}
	
	/**
	 * @param extension
	 * @param value
	 */
	public void addExtension(TLC extension, String value) {
		extensions.put(extension, value);
	}
	
	/**
	 * @param extension
	 * @return
	 */
	public String getExtension(TLC extension) {
		if (extensions.keySet().contains(extension)) {
			return extensions.get(extension);
		}
		return null;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	
	public double getLatitudeRadians() {
		return Math.toRadians(latitude);
	}
	
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	public double getLongitudeRadians() {
		return Math.toRadians(longitude);
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the fixValidity
	 */
	public FixValidity getFixValidity() {
		return fixValidity;
	}

	/**
	 * @param fixValidity the fixValidity to set
	 */
	public void setFixValidity(FixValidity fixValidity) {
		this.fixValidity = fixValidity;
	}

	/**
	 * @return the pressureAltitude
	 */
	public int getPressureAltitude() {
		return pressureAltitude;
	}

	/**
	 * @param pressureAltitude the pressureAltitude to set
	 */
	public void setPressureAltitude(int pressureAltitude) {
		this.pressureAltitude = pressureAltitude;
	}

	/**
	 * @return the gnssAltitude
	 */
	public int getGnssAltitude() {
		return gnssAltitude;
	}

	/**
	 * @param gnssAltitude the gnssAltitude to set
	 */
	public void setGnssAltitude(int gnssAltitude) {
		this.gnssAltitude = gnssAltitude;
	}
}
