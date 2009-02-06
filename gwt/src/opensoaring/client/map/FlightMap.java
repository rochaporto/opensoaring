package opensoaring.client.map;

import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;
import opensoaring.client.map.PolylineEncoder.EncodedPath;

import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;

public class FlightMap extends SimplePanel {
	
	private Flight flight;
	
	private MapWidget mapWidget = new MapWidget();
	
	private int animPosition = 0;
	
	private Marker animationMarker;
	
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
			Fix[] fixes = flight.getFlightFixes().toArray(new Fix[] {});
			/**ArrayList<LatLng> points = new ArrayList<LatLng>();
			for (int i=0; i<fixes.size(); i+=30) {
				points.add(LatLng.newInstance(fixes.get(i).getLatitude(), 
						fixes.get(i).getLongitude()));
			}*/
			PolylineEncoder polyEncoder = new PolylineEncoder();
			EncodedPath encodedPath = polyEncoder.encode(fixes);
			Polyline flightPath = Polyline.fromEncoded(encodedPath.getEncodedPoints(), 
					polyEncoder.getZoomFactor(), encodedPath.getEncodedLevels(), 
					polyEncoder.getNumLevels());
			
			mapWidget.clearOverlays();
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
