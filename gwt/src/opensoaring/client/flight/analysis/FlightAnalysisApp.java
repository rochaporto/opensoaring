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

import java.util.ArrayList;

import opensoaring.client.OpenSoarApp;
import opensoaring.client.igc.analyze.FlightAnalyzer;
import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;
import opensoaring.client.igc.flight.Task;
import opensoaring.client.igc.flight.TaskLeg;
import opensoaring.client.igc.optimize.TP1Optimizer;
import opensoaring.client.json.JsonClient;
import opensoaring.client.json.JsonpListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FlightAnalysisApp extends OpenSoarApp implements JsonpListener {
	
	private VerticalPanel mainPanel = new VerticalPanel();

	private FlowPanel displayPanel = new FlowPanel();
	
	private PanelVisual visualPanel;
	
	private PanelDetails detailsPanel;
	
	private PanelAdditionalInfo additionalInfoPanel;
	
	private int animPosition = 0;
	
	private AnimationTimer animTimer = new AnimationTimer();
	
	private Flight flight;
	
	public FlightAnalysisApp(String caption) {
		super(caption);

		visualPanel = new PanelVisual(this);
		detailsPanel = new PanelDetails(this);
		additionalInfoPanel = new PanelAdditionalInfo(this);
		 
		flight = new Flight();
	
		displayPanel.add(visualPanel);
		displayPanel.add(detailsPanel);			
		mainPanel.add(displayPanel);
		
		additionalInfoPanel.add(new HTML("I"));
		mainPanel.add(additionalInfoPanel);

		mainPanel.setStyleName("openSoarFA-mainPanel");
		setContentPanel(mainPanel);
	}

	private class AnimationTimer extends Timer {
		public void run() {
			if (animPosition < flight.getFlightFixes().size()) {
				//flightMap.moveTo(animPosition);
				//animPositionTime.setText(DateTimeFormat.getMediumTimeFormat().format(flight.getFlightFixes().get(animPosition).getTime()));
				++animPosition;
			} else {
				stop();
			}
		}
	}

	public Flight getFlight() {
		return flight;
	}
	
	public void start() {
		animPosition = 0;
		//flightMap.setZoomLevel(12);
		animTimer.scheduleRepeating(200);
	}
	
	public void stop() {
		animTimer.cancel();
	}
	
	public void onJsonpResponse(String url, JavaScriptObject jsonResponse) {
		JSONObject resp = new JSONObject(jsonResponse);
		flight = new Flight(resp.get("data").isString().stringValue());
		FlightAnalyzer flightAnalyzer = new FlightAnalyzer(flight);
		flightAnalyzer.validate();
		visualPanel.setFlight(flight);
	}
	
	public void optimize() {
		ArrayList<Fix> fixes = flight.getFlightFixes();
		Task task = new TP1Optimizer(flight).optimize();
		

		GWT.log("tp1 optimizer :: " + task.getDistance(), null);
		visualPanel.setTask(task);
	}

	public void setFlight(String url) {
		JsonClient.getJsonp(url, this);
	}
}