package opensoaring.client.igc.flight;

import opensoaring.client.igc.LogParser.TLC;

/**
 * Definition of an extension to a Fix entry in the IGC flight log.
 * 
 * An extension is fully defined by its Three Letter Code (TLC), and the position in the
 * 'B' record - starting at 'startByte' and ending at 'endByte'. * 
 * 
 * @author Ricardo Rocha <rocha@opensoaring.info>
 *
 */
public class FixExtension {
	
	/**
	 * The Three Letter Code (TLC) describing this extension.
	 */
	private TLC extension;
	
	/**
	 * The start byte (position) of the extension's value within the 'B' record in the flight log.
	 */
	private int startByte;
	
	/**
	 * The end byte (position) of the extension's value within the 'B' record in the flight log.
	 */
	private int endByte;
	
	/**
	 * Class constructor.
	 * 
	 * @param extension The Three Letter Code (TLC) describing the extension
	 * @param startByte The start byte of the value of the extension in the 'B' record
	 * @param endByte The end byte of the value of the extension in the 'B' record
	 */
	public FixExtension(TLC extension, int startByte, int endByte) {
		this.extension = extension;
		this.startByte = startByte;
		this.endByte = endByte;
	}

	/**
	 * Returns the Three Letter Code describing the extension.
	 * 
	 * @return The Three Letter Code of the extension
	 */
	public TLC getExtension() {
		return extension;
	}

	/**
	 * Sets the Three Letter Code (TLC) of the extension.
	 * 
	 * @param extension The Three Letter Code (TLC) of the extension
	 */
	public void setExtension(TLC extension) {
		this.extension = extension;
	}

	/**
	 * Returns the start byte (position) of the extension's value in the 'B' record.
	 * 
	 * @return The start byte (position) of the extension's value in the 'B' record
	 */
	public int getStartByte() {
		return startByte;
	}

	/**
	 * Sets the start byte (position) of the extension's value in the 'B' record.
	 * 
	 * @param startByte The start byte (position) of the extension's value in the 'B' record
	 */
	public void setStartByte(int startByte) {
		this.startByte = startByte;
	}

	/**
	 * Returns the end byte (position) of the extension's value in the 'B' record.
	 * 
	 * @return The end byte (position) of the extension's value in the 'B' record
	 */
	public int getEndByte() {
		return endByte;
	}

	/**
	 * Sets the end byte (position) of the extension's value in the 'B' record.
	 * 
	 * @param endByte The end byte (position) of the extension's value in the 'B' record
	 */
	public void setEndByte(int endByte) {
		this.endByte = endByte;
	}
	
}
