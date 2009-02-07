/*
 * Copyright (C) 2008 OpenSoaring <contact@opensoaring.info>.
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

package opensoaring.client.flightanalysis;

import java.util.ArrayList;

import opensoaring.client.OpenSoarApp;
import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;
import opensoaring.client.igc.optimize.FAI3TPOptimizer;
import opensoaring.client.json.JsonClient;
import opensoaring.client.json.JsonpListener;
import opensoaring.client.map.FlightMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlightAnalysisApp extends OpenSoarApp implements ClickListener, JsonpListener {
	
	private VerticalPanel mainPanel = new VerticalPanel();

	private FlowPanel displayPanel = new FlowPanel();
	
	private VerticalPanel visualDisplayPanel = new VerticalPanel();
	
	private VerticalPanel displayControlPanel = new VerticalPanel();
	
	private VerticalPanel moreInfoPanel = new VerticalPanel();
	
	private FlightMap flightMap;
	
	private Button load = new Button("Load");
	
	private Button animStart = new Button("Start");
	
	private Button animStop = new Button("Stop");
	
	private Button optimize = new Button("Optimize");
	
	private Label flightDate = new Label();
	
	private Label pilotInCharge = new Label();
	
	private Label gliderType = new Label();
	
	private Label gliderId = new Label();
	
	private Label registration = new Label();
	
	private Label animPositionTime = new Label();
	
	private int animPosition = 0;
	
	private AnimationTimer animTimer = new AnimationTimer();
	
	private Flight flight;
	
	public FlightAnalysisApp(String caption) {
		super(caption);

		flight = new Flight();
		
		flightMap = new FlightMap();
		visualDisplayPanel.add(flightMap);
		visualDisplayPanel.setStyleName("openSoarFA-visualDisplayPanel");

		displayControlPanel = getDisplayControlPanel();
	
		displayPanel.add(visualDisplayPanel);
		displayPanel.add(displayControlPanel);
		displayPanel.setStyleName("openSoarFA-displayPanel");			
		mainPanel.add(displayPanel);
		
		moreInfoPanel.setStyleName("openSoarFA-moreInfoPanel");
		moreInfoPanel.add(new HTML("I"));
		mainPanel.add(moreInfoPanel);

		mainPanel.setStyleName("openSoarFA-mainPanel");
		setContentPanel(mainPanel);
	}

	public void onClick(Widget sender) {
		if (sender == load) {
			JsonClient.getJsonp("http://localhost:9999/NetCoupe2008_5138.igc?", this);
			//JsonClient.getJsonp("http://localhost:9999/NetCoupe2008_13437.igc?", this);
		} else if(sender == animStart) {
			start();
		} else if(sender == animStop) {
			stop();
		} else if(sender == optimize) {
			optimize();
		}
	}

	public void onJsonpResponse(String url, JavaScriptObject jsonResponse) {
		JSONObject resp = new JSONObject(jsonResponse);
		
		flight = new Flight(resp.get("data").isString().stringValue());
		flight.parseAndValidate();
		flightMap.setFlight(flight);
		flightMap.reset();
		
		redraw();
	}
	
	private VerticalPanel getDisplayControlPanel() {
		VerticalPanel displayControl = new VerticalPanel();

		FlowPanel loadControls = new FlowPanel();
		loadControls.setStyleName("openSoarFA-controlsPanel");
		load.addClickListener(this);
		loadControls.add(load);
		displayControl.add(loadControls);
		
		Grid flightDetails = new Grid();
		flightDetails.setStyleName("openSoarFA-controlsPanel");
		flightDetails.resize(4, 2);
		Label dateTitle = new Label("Flight Date");
		dateTitle.setStyleName("openSoarFA-title");
		flightDetails.setWidget(0, 0, dateTitle);
		flightDetails.setWidget(0, 1, flightDate);
		Label pilotInChargeTitle = new Label("Pilot Name");
		pilotInChargeTitle.setStyleName("openSoarFA-title");
		flightDetails.setWidget(1, 0, pilotInChargeTitle);
		flightDetails.setWidget(1, 1, pilotInCharge);
		Label gliderTypeTitle = new Label("Glider Type");
		gliderTypeTitle.setStyleName("openSoarFA-title");
		flightDetails.setWidget(2, 0, gliderTypeTitle);
		flightDetails.setWidget(2, 1, gliderType);
		Label registrationTitle = new Label("Registration");
		registrationTitle.setStyleName("openSoarFA-title");
		flightDetails.setWidget(3, 0, registrationTitle);
		flightDetails.setWidget(3, 1, registration);
		displayControl.add(flightDetails);
		
		FlowPanel animControls = new FlowPanel();
		animControls.setStyleName("openSoarFA-controlsPanel");
		animStart.addClickListener(this);
		animControls.add(animStart);
		animStop.addClickListener(this);
		animControls.add(animStop);
		optimize.addClickListener(this);
		animControls.add(optimize);
		displayControl.add(animControls);
		
		Grid animDetails = new Grid();
		animDetails.resize(4, 2);
		Label animTime = new Label("Time");
		animTime.setStyleName("openSoarFA-title");
		animDetails.setWidget(0, 0, animTime);
		animDetails.setWidget(0, 1, animPositionTime);
		
		displayControl.add(animDetails);
		displayControl.setStyleName("openSoarFA-displayControlPanel");
		return displayControl;
	}
	
	public void redraw() {
		if (flight != null) {
			flightDate.setText(DateTimeFormat.getFullDateFormat().format(flight.getFlightProps().getFlightDate()));
			pilotInCharge.setText(flight.getFlightProps().getPilotInCharge());
			gliderType.setText(flight.getFlightProps().getGliderType());
			gliderId.setText(flight.getFlightProps().getGliderId());
		}
	}

	private class AnimationTimer extends Timer {
		public void run() {
			if (animPosition < flight.getFlightFixes().size()) {
				flightMap.moveTo(animPosition);
				animPositionTime.setText(DateTimeFormat.getMediumTimeFormat().format(flight.getFlightFixes().get(animPosition).getTime()));
				++animPosition;
			} else {
				stop();
			}
		}
	}

	public void start() {
		animPosition = 0;
		flightMap.setZoomLevel(12);
		animTimer.scheduleRepeating(200);
	}
	
	public void stop() {
		animTimer.cancel();
	}
	
	public void optimize() {
		Fix[] optimizedFixes = new FAI3TPOptimizer().optimize(flight);
		ArrayList<LatLng> legPoints = new ArrayList<LatLng>();
		for (Fix fix: optimizedFixes) {
			GWT.log("START :: " + fix.getLatitude() + " :: END :: " + fix.getLongitude(), null);
			legPoints.add(LatLng.newInstance(fix.getLatitude(), fix.getLongitude()));
		}
		Polyline optimizedPath = new Polyline(legPoints.toArray(new LatLng[] {}));
		PolyStyleOptions optimizedStyle = PolyStyleOptions.newInstance("#000000", 2, 0.8);
		optimizedPath.setStrokeStyle(optimizedStyle);
		flightMap.mapWidget.addOverlay(optimizedPath);
	}
}