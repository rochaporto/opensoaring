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
import opensoaring.client.igc.flight.Task;
import opensoaring.client.igc.flight.TaskLeg;

import com.google.gwt.core.client.GWT;

public class TP2Optimizer extends BaseOptimizer {

	private static String description = "2 Turn Point";
	
	public TP2Optimizer(Flight flight) {
		super(flight);
	}
	
	public String getDescription() {
		return description;
	}

	
	public Task optimize() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		maxDelta = maxDelta();
		
		Task task = new Task(3);
		
		double totalDistance = 0.0;
		double bound = 0.0;
		for (int tp1=1; tp1<fixes.size()-2; ) {
			TaskLeg firstLeg = furthestFrom(tp1, 0, tp1, 0.0);
			for (int tp2=tp1+1; tp2<fixes.size()-1; ) {
				TaskLeg secondLeg = new TaskLeg(tp1, tp2, 
						LogUtil.distance(fixes.get(tp1), fixes.get(tp2)));
				TaskLeg thirdLeg = furthestFrom(tp2, tp2+1, fixes.size()-1, 0.0);
				totalDistance = firstLeg.getDistance() + secondLeg.getDistance()
					+ thirdLeg.getDistance();
				if (totalDistance > bound) {
					bound = totalDistance;
					task.setTaskLeg(0, firstLeg);
					task.setTaskLeg(1, secondLeg);
					task.setTaskLeg(2, thirdLeg);
					++tp2;
				} else {
					tp2 = forward(tp2, 0.5 * (bound-totalDistance));
				}
			}
			++tp1;
		}
		GWT.log("internal dist :: " + task.getDistance(), null);
		return task;
	}	

}
