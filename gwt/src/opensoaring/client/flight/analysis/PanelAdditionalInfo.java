package opensoaring.client.flight.analysis;

import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelAdditionalInfo extends VerticalPanel {

	private FlightAnalysisApp flightAnalysisApp;
	
	public PanelAdditionalInfo(FlightAnalysisApp flightAnalysisApp) {

		this.flightAnalysisApp = flightAnalysisApp;
		
		setStyleName("openSoarFA-moreInfoPanel");
	}
}
