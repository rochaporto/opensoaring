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
package opensoaring.client.igc.analyze;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import opensoaring.client.igc.LogUtil;
import opensoaring.client.igc.analyze.FlightPhase.PhaseType;
import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;

public class FlightAnalyzer {

	private int takeoffPosition = -1;
	
	private int landingPosition = -1;
	
	private int minAltDelta = 10;
	
	private int minSpeed = 40;
	
	private int maxSpeed = 300;
	
	private Flight flight;
	
	private FlightPhase[] flightPhases;
	
	public FlightAnalyzer(Flight flight) {
		this.flight = flight;
		
		// Make sure the flight data is parsed and validated
		this.flight.parse();
		this.validate();
	}

	public void validate() {
		ArrayList<Fix> flightFixes = flight.getFlightFixes();
		
		for (int i=1; i<flightFixes.size(); i++) {
			double speed = LogUtil.groundSpeed(flightFixes.get(i-1), flightFixes.get(i));
			if (speed > maxSpeed) {
				flightFixes.remove(i);
				++i;
			}
		}
	}
	
	public void analyze() {
		
		ArrayList<Fix> flightFixes = flight.getFlightFixes();
		// Start by computing takeoff and landing positions
		computeTakeoffAndLanding();
		GWT.log("TAKEOFF :: " + flight.getFlightFixes().get(takeoffPosition).getTime(), null);
		GWT.log("LANDING :: " + flight.getFlightFixes().get(landingPosition).getTime(), null);
		
		// Create an initial 'STRAIGHT' phase (start at takeoff, no end filled yet) 
		FlightPhase currentPhase = new FlightPhase(PhaseType.STRAIGHT, takeoffPosition, -1);
		
		// Compute each of the flight phases
		for(int i=takeoffPosition; i<landingPosition; i++) {
			double course1 = LogUtil.course(flightFixes.get(i), flightFixes.get(i+1));
			double course2 = LogUtil.course(flightFixes.get(i), flightFixes.get(i+1));
			double courseChange = course2 - course1;
			while (courseChange < 40) {
				++i;
				course1 = LogUtil.course(flightFixes.get(i), flightFixes.get(i+1));
				course2 = LogUtil.course(flightFixes.get(i+1), flightFixes.get(i+2));
				courseChange = course2 - course1;	
			}
			GWT.log("1 :: " + flightFixes.get(i).getTime() + " :: " + flightFixes.get(i).getGnssAltitude() + " :: " + course1, null);
			GWT.log("2 :: " + flightFixes.get(i+1).getTime() + " :: " + flightFixes.get(i+1).getGnssAltitude() + " :: " + course2, null);
			GWT.log("3 :: " + flightFixes.get(i+2).getTime() + " :: " +flightFixes.get(i+2).getGnssAltitude() + " :: " + courseChange, null);
			break;
		}
	}
	
	private void computeTakeoffAndLanding() {
		ArrayList<Fix> flightFixes = flight.getFlightFixes();
		
		// Computing takeoff position (if it is not assigned already, this allows for setting
		// alternative takeoff positions)
		if (takeoffPosition == -1) {
			int moveStart = 0;
			for (int i=1; i<flight.getFlightFixes().size(); i++) {
				if (flightFixes.get(i).getGnssAltitude() - flightFixes.get(moveStart).getGnssAltitude()
						<= 0) {
					moveStart = i;
				} else if (flightFixes.get(i).getGnssAltitude() - 
						flightFixes.get(moveStart).getGnssAltitude() > minAltDelta) {
					this.takeoffPosition = moveStart;
					break;
				}
			}
		}
		
		// Computing landing position (if it is not assigned already, this allows for setting
		// alternative landing positions)
		if (landingPosition == -1) {
			int moveEnd = flight.getFlightFixes().size()-1;
			for (int i=moveEnd; i>0; i--) {
				if (flightFixes.get(i-1).getGnssAltitude() 
						- flightFixes.get(i).getGnssAltitude() <= 0) {
					moveEnd = i;
				} else if (flightFixes.get(i-1).getGnssAltitude() - 
						flightFixes.get(moveEnd).getGnssAltitude() > minAltDelta) {
					this.landingPosition = moveEnd;
					break;
				}
			}
		}
	}
	
	/**
	 * @return the flight
	 */
	public Flight getFlight() {
		return flight;
	}

	/**
	 * @param flight the flight to set
	 */
	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	/**
	 * @return the flightPhases
	 */
	public FlightPhase[] getFlightPhases() {
		return flightPhases;
	}

	/**
	 * @param flightPhases the flightPhases to set
	 */
	public void setFlightPhases(FlightPhase[] flightPhases) {
		this.flightPhases = flightPhases;
	}

	/**
	 * @return the takeoffPosition
	 */
	public int getTakeoffPosition() {
		return takeoffPosition;
	}

	/**
	 * @param takeoffPosition the takeoffPosition to set
	 */
	public void setTakeoffPosition(int takeoffPosition) {
		this.takeoffPosition = takeoffPosition;
	}

	/**
	 * @return the landingPosition
	 */
	public int getLandingPosition() {
		return landingPosition;
	}

	/**
	 * @param landingPosition the landingPosition to set
	 */
	public void setLandingPosition(int landingPosition) {
		this.landingPosition = landingPosition;
	}

	/**
	 * @return the minAltDelta
	 */
	public int getMinAltDelta() {
		return minAltDelta;
	}

	/**
	 * @param minAltDelta the minAltDelta to set
	 */
	public void setMinAltDelta(int minAltDelta) {
		this.minAltDelta = minAltDelta;
	}

	/**
	 * @return the minSpeed
	 */
	public int getMinSpeed() {
		return minSpeed;
	}

	/**
	 * @param minSpeed the minSpeed to set
	 */
	public void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
	}

	/**
	 * @return the maxSpeed
	 */
	public int getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
