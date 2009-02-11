package opensoaring.client.flight.analysis;

import java.util.ArrayList;

import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;
import opensoaring.client.igc.flight.Task;
import opensoaring.client.igc.flight.TaskLeg;
import opensoaring.client.map.FlightMap;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

public class PanelVisual extends Composite implements AnimationListener {

	private SimplePanel mainPanel = new SimplePanel();
	
	private FlightAnalysisApp flightAnalysisApp;
	
	private FlightMap flightMap;
	
	public PanelVisual(FlightAnalysisApp flightAnalysisApp) {
		this.flightAnalysisApp = flightAnalysisApp;
		
		flightMap = new FlightMap();
		mainPanel.add(flightMap);

		initWidget(mainPanel);
		setStyleName("openSoarFA-visualDisplayPanel");
	}

	public void setFlight(Flight flight) {
		flightMap.setFlight(flight);
		flightMap.reset();
	}
	
	public void setTask(Task task) {
		ArrayList<Fix> fixes = flightMap.getFlight().getFlightFixes();
		ArrayList<LatLng> points = new ArrayList<LatLng>();
		for (TaskLeg leg: task.getTaskLegs()) {
			points.add(LatLng.newInstance(fixes.get(leg.getStartIndex()).getLatitude(), 
						fixes.get(leg.getStartIndex()).getLongitude()));
			points.add(LatLng.newInstance(fixes.get(leg.getEndIndex()).getLatitude(), 
					fixes.get(leg.getEndIndex()).getLongitude()));
		}
		Polyline optimizedPath = new Polyline(points.toArray(new LatLng[] {}));
		PolyStyleOptions optimizedStyle = PolyStyleOptions.newInstance("#000000", 2, 0.8);
		optimizedPath.setStrokeStyle(optimizedStyle);
		flightMap.mapWidget.addOverlay(optimizedPath);	
	}
	
	public void onAnimationUpdate(AnimationInfo animInfo) {
		// TODO Auto-generated method stub
		
	}
}
