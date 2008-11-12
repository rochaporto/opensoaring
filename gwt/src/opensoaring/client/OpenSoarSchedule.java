package opensoaring.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenSoarSchedule extends OpenSoarApp implements ClickListener {
	
	private VerticalPanel timedSchedPanel = new VerticalPanel();
	private HorizontalPanel timedSchedNavPanel = new HorizontalPanel();
	private DeckPanel timedSchedContentPanel = new DeckPanel();
	
	private ToggleButton fourDaysNavButton = new ToggleButton("4 Days");
	private ToggleButton weekNavButton = new ToggleButton("Week");
	private ToggleButton monthNavButton = new ToggleButton("Month");
	
	private Button infoButton = new Button("Info Msg");
	private Button errorButton = new Button("Error Msg");
	
	public OpenSoarSchedule(String caption) {
		super(caption);
		
		// Build the Schedule application panel
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

		FlowPanel fp = new FlowPanel();
		infoButton.addClickListener(this);
		fp.add(infoButton);
		errorButton.addClickListener(this);
		fp.add(errorButton);
		timedSchedContentPanel.add(fp);
		timedSchedContentPanel.showWidget(0);
		fourDaysNavButton.setDown(true);
		timedSchedContentPanel.setStyleName("contentSchedPanel");
		timedSchedPanel.add(timedSchedContentPanel);
		
		// Add it to the app's content panel
		this.setContentPanel(timedSchedPanel);
	}
	
	public void onClick(Widget sender) {
		if (sender == this.infoButton) {
			info("You clicked the info msg button");
		} else if(sender == this.errorButton) {
			error("You clicked the error msg button");
		}
	}
	
}