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

import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;
import opensoaring.client.igc.flight.Task;
import opensoaring.client.igc.flight.TaskLeg;

public class TP1Optimizer extends BaseOptimizer {

	private static String description = "FAI 3 Turn Point";
	
	public TP1Optimizer(Flight flight) {
		super(flight);
	}
	
	public String getDescription() {
		return description;
	}

	
	public Task optimize() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		maxDelta = maxDelta();
		
		Task task = new Task(2);
		
		double totalDistance = 0.0;
		double bound = 0.0;
		for (int tp1=1; tp1<fixes.size()-1; ) {
			TaskLeg firstLeg = furthestFrom(tp1, 0, tp1, 0.0);
			TaskLeg secondLeg = furthestFrom(tp1, tp1+1, fixes.size()-1, 0.0);
			totalDistance = firstLeg.getDistance() + secondLeg.getDistance();
			//GWT.log("before :: " + before + " :: after :: " + after, null);
			if (totalDistance > bound) {
				bound = totalDistance;
				GWT.log("intermediate :: " + firstLeg.getDistance() + " :: " + secondLeg.getDistance(), null);
				task.setTaskLeg(0, firstLeg);
				task.setTaskLeg(1, secondLeg);
				++tp1;
			} else {
				//GWT.log("tp1 would be (" + forward(tp1, bound - total) + ", " + (bound - total), null);
				tp1 = forward(tp1, bound-totalDistance);
			}
		}
		GWT.log("internal dist :: " + task.getDistance(), null);
		return task;
	}	

}
