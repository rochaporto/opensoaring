package opensoaring.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpenSoaring implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel navPanel = new HorizontalPanel();
	private HorizontalPanel headerPanel = new HorizontalPanel();
	private DeckPanel contentPanel = new DeckPanel();
	
	private ToggleButton mainNavButton = new ToggleButton("Home");
	private ToggleButton schedNavButton = new ToggleButton("Schedule");
	private ToggleButton flightNavButton = new ToggleButton("Flight Info");
	
	public void onModuleLoad() {

		// Header Panel
		headerPanel.setStyleName("headerPanel");
		
		// Navigation Panel
		HorizontalPanel navItemsPanel = new HorizontalPanel();
		mainNavButton.setStylePrimaryName("navButton");
		schedNavButton.setStylePrimaryName("navButton");
		flightNavButton.setStylePrimaryName("navButton");
		navItemsPanel.add(mainNavButton);
		navItemsPanel.add(schedNavButton);
		navItemsPanel.add(flightNavButton);
		navPanel.add(navItemsPanel);
		navPanel.setStyleName("navPanel");
		
		// Content Panel + Adding Applications
		contentPanel.add(new ScheduleApp("Home"));
		contentPanel.add(new ScheduleApp("Schedule"));
		contentPanel.add(new ScheduleApp("Flight Info"));
		contentPanel.showWidget(0);
		contentPanel.setStyleName("contentPanel");
		
		// Main Panel
		mainPanel.add(headerPanel);
		mainPanel.add(navPanel);
		mainPanel.add(contentPanel);
		mainPanel.setStyleName("mainPanel");
		
		RootPanel.get().add(mainPanel);		
	  
	}
}
