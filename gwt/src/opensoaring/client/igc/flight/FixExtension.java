package opensoaring.client.igc.flight;

import opensoaring.client.igc.LogParser.TLC;

public class FixExtension {
	
	private TLC extension;
	
	private int startByte;
	
	private int endByte;
	
	public FixExtension(TLC extension, int startByte, int endByte) {
		this.extension = extension;
		this.startByte = startByte;
		this.endByte = endByte;
	}

	/**
	 * @return the extension
	 */
	public TLC getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(TLC extension) {
		this.extension = extension;
	}

	/**
	 * @return the startByte
	 */
	public int getStartByte() {
		return startByte;
	}

	/**
	 * @param startByte the startByte to set
	 */
	public void setStartByte(int startByte) {
		this.startByte = startByte;
	}

	/**
	 * @return the endByte
	 */
	public int getEndByte() {
		return endByte;
	}

	/**
	 * @param endByte the endByte to set
	 */
	public void setEndByte(int endByte) {
		this.endByte = endByte;
	}
	
	public String toString() {
		return extension + " :: [" + startByte + "," + endByte + "]";
	}
}
