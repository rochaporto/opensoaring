/*
 * Copyright (C) 2009 OpenSoaring <contact@opensoaring.info>.
 * 
 * This file is part of OpenSoaring.
 *
 * OpenSoaring is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenSoaring is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenSoaring.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package opensoaring.client.flight.analysis;

public class FlightPhase {

	public static enum PhaseType {
		CIRCLE, STRAIGHT, TOW
	}
	
	private PhaseType type;
	
	private int start;
	
	private int end;
	
	public FlightPhase() {
		
	}
	
	public FlightPhase(PhaseType type, int start, int end) {
		this.type = type;
		this.start = start;
		this.end = end;		
	}

	/**
	 * @return the type
	 */
	public PhaseType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(PhaseType type) {
		this.type = type;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	
}
