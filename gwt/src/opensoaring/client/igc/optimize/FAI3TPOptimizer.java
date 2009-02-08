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

import opensoaring.client.igc.LogUtil;
import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;

public class FAI3TPOptimizer implements FlightOptimizer {

	private static String description = "FAI 3 Turn Point";
	
	public FAI3TPOptimizer() {
		
	}
	
	public String getDescription() {
		return description;
	}

	public Fix[] optimize(Flight flight) {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		
		ArrayList<Fix> optimizedFixes = new ArrayList<Fix>();
		
		// Calculate all possible flight legs
		int startIndex = 0;
		optimizedFixes.add(fixes.get(startIndex));
		for (int i=0; i<3; i++) {
			double maxDist = 0;
			int endIndex = startIndex+1;
			for (int j=endIndex; j<fixes.size(); j++) {
				double distance = LogUtil.distance(fixes.get(startIndex), fixes.get(j));
				if (distance > maxDist) {
					maxDist = distance;
					endIndex = j;
				}
			}
			optimizedFixes.add(fixes.get(endIndex));
			startIndex = endIndex;	
		}
		
		// Choose the sequence of 4 legs which gives the best distance
		// TODO:
		
		return optimizedFixes.toArray(new Fix[] {});
	}

}
