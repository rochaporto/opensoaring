package opensoaring.client.flight.analysis;

import opensoaring.client.igc.flight.Flight;
import opensoaring.client.map.FlightMap;

import com.google.gwt.core.client.GWT;
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
	
	public void onAnimationUpdate(AnimationInfo animInfo) {
		// TODO Auto-generated method stub
		
	}
}
