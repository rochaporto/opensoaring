package opensoaring.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenSoaring extends Composite implements EntryPoint, ClickListener {

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel headerPanel = new HorizontalPanel();	
	private HorizontalPanel navItemsPanel = new HorizontalPanel();
	private DeckPanel contentPanel = new DeckPanel();
	
	public void onModuleLoad() {

		// Header Panel
		headerPanel.setStyleName("openSoar-headerPanel");
		
		// Navigation Panel
		HorizontalPanel navPanel = new HorizontalPanel();
		navPanel.add(navItemsPanel);
		navPanel.setStyleName("openSoar-navPanel");
		
		// Content Panel + Adding Applications
		this.addApplication("Home", new OpenSoarApp("Home"));
		this.addApplication("Schedule", new OpenSoarSchedule("Schedule"));
		this.addApplication("Flight Info", new OpenSoarApp("Flight Info"));
		this.setSelectedApp(0);
		contentPanel.setStyleName("openSoar-contentPanel");
		
		// Main Panel
		mainPanel.add(headerPanel);
		mainPanel.add(navPanel);
		mainPanel.add(contentPanel);
		mainPanel.setStyleName("openSoar-mainPanel");
		
		RootPanel.get().add(mainPanel); 
	}
	
	protected void addApplication(String name, Widget w) {
		ToggleButton appButton = new ToggleButton(name);
		appButton.addClickListener(this);
		appButton.setStylePrimaryName("openSoar-navButton");
		this.navItemsPanel.add(appButton);
		this.contentPanel.add(w);
	}
	
	public void setSelectedApp(int index) {
		for (int i=0; i<this.navItemsPanel.getWidgetCount(); i++) {
			if (i == index) {
				((ToggleButton)this.navItemsPanel.getWidget(i)).setDown(true);
			} else {
				((ToggleButton)this.navItemsPanel.getWidget(i)).setDown(false);
			}
		}
		this.contentPanel.showWidget(index);
	}
	
	public void onClick(Widget sender) {
		ToggleButton appButton = (ToggleButton)sender;
		for (int i=0; i < this.navItemsPanel.getWidgetCount(); i++) {
			ToggleButton tmpButton = (ToggleButton)this.navItemsPanel.getWidget(i);
			if (tmpButton.equals(appButton)) {				
				this.setSelectedApp(i);
			}
		}
	}
}
