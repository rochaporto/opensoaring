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
package opensoaring.client.igc.optimize;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import opensoaring.client.igc.LogUtil;
import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;

public class FAI3TPOptimizer implements FlightOptimizer {

	private static String description = "FAI 3 Turn Point";
	
	private Flight flight;
	
	private double maxDelta;
	
	public FAI3TPOptimizer(Flight flight) {
		this.flight = flight;
	}
	
	public String getDescription() {
		return description;
	}

	private double maxDelta() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		double maxDelta = 0;
		for (int i=0; i<fixes.size()-1; i++) {
			maxDelta = Math.max(maxDelta, LogUtil.distance(fixes.get(i), fixes.get(i+1)));
		}
		return maxDelta;
	}
	
	private int forward(int startFrom, double distance) {
		return (int)(distance / maxDelta) + startFrom;
	}
	
	private double furthestFrom(int point, int startPoint, int endPoint, double minDistance) {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		int furthestPoint = -1;
		double maxDistance = minDistance;
		
		for (int i=startPoint; i<endPoint; i++) {
			double distance = LogUtil.distance(fixes.get(point), fixes.get(i));
			//GWT.log("i=" + i + " :: dist=" + distance, null);
			if (distance > maxDistance) {
				//GWT.log("updated dist=" + maxDistance, null);
				maxDistance = distance;
				furthestPoint = i;
			} else {
				int step = forward(i, maxDistance - distance);
				//GWT.log("forward step=" + step, null);
				i = step;
			}
		}
		return maxDistance;
	}
	
	private double maxDistanceFromTakeoff() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		
		return furthestFrom(0, 1, fixes.size(), 0.0);
	}
	
	private double openDistance() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		
		double distance = 0.0;
		for (int i=0; i<fixes.size(); i++) {
			distance = furthestFrom(i, i+1, fixes.size(), distance);
		}
		return distance;
	}
	
	public Fix[] optimize() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		maxDelta = maxDelta();
		
		GWT.log("max delta :: " + maxDelta, null);
		GWT.log("max dist from takeoff :: " + maxDistanceFromTakeoff(), null);
		GWT.log("open distance :: " + openDistance(), null);
		return new Fix[] {};
	}
	

}
