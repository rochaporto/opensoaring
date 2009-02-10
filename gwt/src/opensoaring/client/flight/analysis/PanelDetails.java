package opensoaring.client.flight.analysis;

import opensoaring.client.igc.flight.Flight;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PanelDetails extends Composite implements AnimationListener, ClickListener {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private FlightAnalysisApp flightAnalysisApp;
	
	private Button load = new Button("Load");
	
	private Button animStart = new Button("Start");
	
	private Button animStop = new Button("Stop");
	
	private Button optimize = new Button("Optimize");
	
	private Label flightDate = new Label();
	
	private Label pilotInCharge = new Label();
	
	private Label gliderType = new Label();
	
	private Label gliderId = new Label();
	
	private Label animPositionTime = new Label();
	
	public PanelDetails(FlightAnalysisApp flightAnalysisApp) {

		this.flightAnalysisApp = flightAnalysisApp;

		Flight flight = flightAnalysisApp.getFlight();
		if (flight != null) {
			flightDate.setText(DateTimeFormat.getFullDateFormat().format(flight.getFlightProps().getFlightDate()));
			pilotInCharge.setText(flight.getFlightProps().getPilotInCharge());
			gliderType.setText(flight.getFlightProps().getGliderType());
			gliderId.setText(flight.getFlightProps().getGliderId());
		}
		
		load.addClickListener(this);
		mainPanel.add(load);
		animStart.addClickListener(this);
		mainPanel.add(animStart);
		animStop.addClickListener(this);
		mainPanel.add(animStop);
		optimize.addClickListener(this);
		mainPanel.add(optimize);
		mainPanel.setStyleName("openSoarFA-detailsPanel");
		initWidget(mainPanel);
	}

	public void onClick(Widget sender) {
		GWT.log("on click", null);
		if (sender == load) {
			flightAnalysisApp.setFlight("http://localhost:9999/NetCoupe2008_5138.igc?");
		} else if(sender == animStart) {
			flightAnalysisApp.start();
		} else if(sender == animStop) {
			flightAnalysisApp.stop();
		} else if(sender == optimize) {
			flightAnalysisApp.optimize();
		}
	}

	public void onAnimationUpdate(AnimationInfo animInfo) {
		// TODO Auto-generated method stub
		
	}

}
