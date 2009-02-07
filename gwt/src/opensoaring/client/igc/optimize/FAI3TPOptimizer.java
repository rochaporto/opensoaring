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
