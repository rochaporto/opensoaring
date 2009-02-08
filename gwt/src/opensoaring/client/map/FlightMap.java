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
package opensoaring.client.map;

import java.util.ArrayList;

import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;
import opensoaring.client.igc.flight.FlightDeclaration;
import opensoaring.client.map.PolylineEncoder.EncodedPath;

import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.ui.SimplePanel;

public class FlightMap extends SimplePanel {
	
	private Flight flight;
	
	public MapWidget mapWidget = new MapWidget();
	
	private int animPosition = 0;
	
	private Marker animationMarker;
	
	private PolyStyleOptions flightPathStyle = PolyStyleOptions.newInstance("#0000ff", 3, 0.6);
	
	private PolyStyleOptions taskPathStyle = PolyStyleOptions.newInstance("#ff0000", 2, 0.5);
	
	public FlightMap() {
		 
		mapWidget = new MapWidget( LatLng.newInstance(0, 0), 1);
		mapWidget.setStyleName("openSoarFA-map2D");
		mapWidget.setCurrentMapType(MapType.getPhysicalMap());
		mapWidget.setScrollWheelZoomEnabled(true);
		mapWidget.addControl(new LargeMapControl());
		
		animationMarker = new Marker(mapWidget.getCenter());
		mapWidget.addOverlay(animationMarker);
		
		this.setWidget(mapWidget);
	}
	
	public void reset() {
		if (flight != null) {

			mapWidget.clearOverlays();
			
			Fix[] fixes = flight.getFlightFixes().toArray(new Fix[] {});
			
			PolylineEncoder polyEncoder = new PolylineEncoder();
			EncodedPath encodedPath = polyEncoder.encode(fixes);
			Polyline flightPath = Polyline.fromEncoded(encodedPath.getEncodedPoints(), 
					polyEncoder.getZoomFactor(), encodedPath.getEncodedLevels(), 
					polyEncoder.getNumLevels());
			flightPath.setStrokeStyle(flightPathStyle);
			
			FlightDeclaration flightDeclaration = flight.getFlightProps().getFlightDeclaration();
			if (flightDeclaration.getNumberTurnpoints() > 0) {
				ArrayList<LatLng> taskPoints = new ArrayList<LatLng>();
				taskPoints.add(LatLng.newInstance(flightDeclaration.getStart().getLatitude(), 
						flightDeclaration.getStart().getLongitude()));
				for (Fix turnPoint: flightDeclaration.getTurnPoints()) {
					taskPoints.add(LatLng.newInstance(turnPoint.getLatitude(), 
							turnPoint.getLongitude()));
				}
				taskPoints.add(LatLng.newInstance(flightDeclaration.getFinish().getLatitude(), 
						flightDeclaration.getFinish().getLongitude()));
				Polyline taskPath = new Polyline(taskPoints.toArray(new LatLng[] {}));
				taskPath.setStrokeStyle(taskPathStyle);
				mapWidget.addOverlay(taskPath);
			}
			
			mapWidget.addOverlay(flightPath);
			mapWidget.setCenter(flightPath.getBounds().getCenter());
			mapWidget.setZoomLevel(mapWidget.getBoundsZoomLevel(flightPath.getBounds()));
		}
	}
	
	public void setZoomLevel(int zoomLevel) {
		mapWidget.setZoomLevel(zoomLevel);
	}
	
	public void moveTo(int position) {
		this.animPosition = position;
		Fix animationFix = flight.getFlightFixes().get(animPosition);
		animationMarker.setLatLng(LatLng.newInstance(animationFix.getLatitude(), 
				animationFix.getLongitude()));
		if (animPosition % 30 == 0) {
			mapWidget.panTo(LatLng.newInstance(animationFix.getLatitude(), 
					animationFix.getLongitude()));
		}
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}
	
	public Flight getFlight() {
		return flight;
	}
	
}
