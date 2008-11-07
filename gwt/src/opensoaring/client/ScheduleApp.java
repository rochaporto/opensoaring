package opensoaring.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ScheduleApp extends Composite {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel headerPanel = new HorizontalPanel();
	private VerticalPanel timedSchedPanel = new VerticalPanel();
	private HorizontalPanel timedSchedNavPanel = new HorizontalPanel();
	private DeckPanel timedSchedContentPanel = new DeckPanel();
	
	private ToggleButton fourDaysNavButton = new ToggleButton("4 Days");
	private ToggleButton weekNavButton = new ToggleButton("Week");
	private ToggleButton monthNavButton = new ToggleButton("Month");
	
	public ScheduleApp(String caption) {
	
		// App Header Panel
		HTML appHeaderTitle = new HTML(caption);
		appHeaderTitle.setStyleName("appHeaderTitle");
		headerPanel.add(appHeaderTitle);
		headerPanel.setStyleName("appHeaderPanel");
		mainPanel.add(headerPanel);
		
		// App Navigation Panel
		HorizontalPanel navItemsPanel = new HorizontalPanel();
		fourDaysNavButton.setStylePrimaryName("navSchedButton");
		weekNavButton.setStylePrimaryName("navSchedButton");
		monthNavButton.setStylePrimaryName("navSchedButton");
		navItemsPanel.add(fourDaysNavButton);
		navItemsPanel.add(weekNavButton);
		navItemsPanel.add(monthNavButton);
		timedSchedNavPanel.add(navItemsPanel);
		timedSchedNavPanel.setStyleName("navSchedPanel");		
		timedSchedPanel.add(timedSchedNavPanel);		
		
		// App Content Panel(s)
		timedSchedContentPanel.add(new HTML("Text"));
		timedSchedContentPanel.showWidget(0);
		fourDaysNavButton.setDown(true);
		timedSchedContentPanel.setStyleName("contentSchedPanel");
		timedSchedPanel.add(timedSchedContentPanel);
		
		// App Panel
		timedSchedPanel.setStyleName("schedPanel");
		mainPanel.add(timedSchedPanel);
		
		// Finish up the composite
		initWidget(mainPanel);
		
		setStyleName("appPanel");
	}
	
}