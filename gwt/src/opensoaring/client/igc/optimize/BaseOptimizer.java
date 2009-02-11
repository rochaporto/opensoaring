package opensoaring.client.igc.optimize;

import java.util.ArrayList;

import opensoaring.client.igc.LogUtil;
import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;
import opensoaring.client.igc.flight.TaskLeg;

public abstract class BaseOptimizer implements FlightOptimizer {

	protected Flight flight;

	protected double maxDelta;
		
	protected BaseOptimizer(Flight flight) {
		this.flight = flight;
	}
	
	protected double maxDelta() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		double maxDelta = 0;
		for (int i=0; i<fixes.size()-1; i++) {
			maxDelta = Math.max(maxDelta, LogUtil.distance(fixes.get(i), fixes.get(i+1)));
		}
		return maxDelta;
	}
	
	protected int forward(int startFrom, double distance) {
		int step = (int)(distance / maxDelta);
		return step > 0 ? startFrom + step: ++startFrom;
	}
	
	protected TaskLeg furthestFrom(int point, int startPoint, int endPoint, double minDistance) {
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
		return new TaskLeg(point, furthestPoint, maxDistance);
	}
	
	private double maxDistanceFromTakeoff() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		
		return furthestFrom(0, 1, fixes.size(), 0.0).getDistance();
	}

	private double openDistance() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		
		double distance = 0.0;
		for (int i=0; i<fixes.size(); i++) {
			distance = furthestFrom(i, i+1, fixes.size(), distance).getDistance();
		}
		return distance;
	}
	
}
